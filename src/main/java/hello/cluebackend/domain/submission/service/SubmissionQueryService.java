package hello.cluebackend.domain.submission.service;

import hello.cluebackend.application.classroom.mapper.ClassRoomMapper;
import hello.cluebackend.domain.assignment.model.Assignment;
import hello.cluebackend.application.submission.dto.request.SubmissionAttachmentUrlDto;
import hello.cluebackend.domain.submission.model.FileType;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.submission.model.Submission;
import hello.cluebackend.domain.submission.model.SubmissionAttachment;
import hello.cluebackend.infrastructure.persistence.submission.SubmissionJpaRepository;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.classroom.service.ClassRoomQueryService;
import hello.cluebackend.domain.classroomuser.service.ClassroomUserService;
import hello.cluebackend.infrastructure.persistence.submissionattachment.SubmissionAttachmentJpaRepository;
import hello.cluebackend.domain.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmissionQueryService {
  private final SubmissionJpaRepository submissionJpaRepository;
  private final SubmissionAttachmentJpaRepository submissionAttachmentJpaRepository;
  private final ClassroomUserService classroomUserService;
  private final ClassRoomQueryService classRoomQueryService;
  private final FileService fileService;
  private final SubmissionCommandService submissionCommandService;
  private final ClassRoomMapper classRoomMapper;

  // 해당 교실 모든 학생에게 과제 부여 & 제출 과제 생성
  @Transactional
  public void assignToAllStudentsInClassroom(UUID userId, UUID classroomId, Assignment assignment){
    ClassRoom classRoom = classRoomMapper.fromClassRoomDtoToEntity(classRoomQueryService.findById(userId ,classroomId));
    List<UserEntity> users = classroomUserService.findAllClassroomUser(classRoom);
    List<Submission> submissions = users.stream()
            .map(u -> new Submission(assignment, u,assignment.getClassRoom(), false, null))
            .toList();
    submissionJpaRepository.saveAll(submissions);
  }

  // 과제 제출하기
  @Transactional
  public Submission submitSubmission(UUID userId, UUID submissionId) {
    Submission submission = submissionCommandService.findByIdOrThrow(submissionId);
    submission.submit();
    return submission;
  }

  // 과제 제출 취소하기
  @Transactional
  public Submission cancelSubmission(UUID userId, UUID submissionId) {
    Submission submission = submissionCommandService.findByIdOrThrow(submissionId);
    submission.cancel();
    return submission;
  }

  // 첨부 파일 추가
  @Transactional
  public SubmissionAttachment fileUpload(UUID userId, UUID submissionId, MultipartFile file) throws IOException {
    Submission submission = submissionCommandService.findByIdOrThrow(submissionId);

    String storedFiledName = fileService.storeFile(file);

    SubmissionAttachment result = SubmissionAttachment.builder()
            .submission(submission)
            .type(FileType.FILE)
            .value(storedFiledName)
            .originalFileName(file.getOriginalFilename())
            .contentType(file.getContentType())
            .size(file.getSize())
            .build();
    submissionAttachmentJpaRepository.save(result);
    return result;
  }

  // 첨부 링크 추가
  @Transactional
  public SubmissionAttachment linkUpload(UUID userId, UUID submissionId, SubmissionAttachmentUrlDto dto) {
    Submission submission = submissionCommandService.findByIdOrThrow(submissionId);

    SubmissionAttachment submissionAttachment = SubmissionAttachment.builder()
            .submission(submission)
            .type(FileType.URL)
            .value(dto.url())
            .build();

    submissionAttachmentJpaRepository.save(submissionAttachment);
    return submissionAttachment;
  }

  // 첨부 파일 삭제
  @Transactional
  public void deleteSubmissionAttachment(UUID userId, UUID submissionAttachmentId) {
    SubmissionAttachment submissionAttachment = submissionCommandService.findSubmissionAttachmentByIdOrThrow(submissionAttachmentId);
    if(submissionAttachment.getType() == FileType.FILE){
      fileService.deleteFile(submissionAttachment.getValue());
    }
    submissionAttachmentJpaRepository.delete(submissionAttachment);
  }
}