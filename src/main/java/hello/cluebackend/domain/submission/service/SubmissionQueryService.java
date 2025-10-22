package hello.cluebackend.domain.submission.service;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.submission.controller.dto.request.SubmissionAttachmentUrlDto;
import hello.cluebackend.domain.submission.domain.FileType;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.submission.domain.Submission;
import hello.cluebackend.domain.submission.domain.SubmissionAttachment;
import hello.cluebackend.domain.submission.domain.repository.SubmissionRepository;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.domain.classroomuser.service.ClassroomUserService;
import hello.cluebackend.domain.submission.domain.repository.SubmissionAttachmentRepository;
import hello.cluebackend.domain.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmissionQueryService {
  private final SubmissionRepository submissionRepository;
  private final SubmissionAttachmentRepository submissionAttachmentRepository;
  private final ClassroomUserService classroomUserService;
  private final ClassRoomService classRoomService;
  private final FileService fileService;
  private final SubmissionCommandService submissionCommandService;

  // 해당 교실 모든 학생에게 과제 부여 & 제출 과제 생성
  @Transactional
  public void assignToAllStudentsInClassroom(UUID userId, UUID classroomId, Assignment assignment){
    ClassRoom classRoom = classRoomService.findById(userId ,classroomId).toEntity();
    List<UserEntity> users = classroomUserService.findAllClassroomUser(classRoom);
    List<Submission> submissions = users.stream()
            .map(u -> new Submission(assignment, u,assignment.getClassRoom(), false, null))
            .toList();
    submissionRepository.saveAll(submissions);
  }

  // 과제 제출하기
  @Transactional
  public Submission submitSubmission(UUID submissionId) {
    Submission submission = submissionCommandService.findByIdOrThrow(submissionId);
    submission.submit();
    return submission;
  }

  // 과제 제출 취소하기
  @Transactional
  public Submission cancelSubmission(UUID submissionId) {
    Submission submission = submissionCommandService.findByIdOrThrow(submissionId);
    submission.cancel();
    return submission;
  }

  // 첨부 파일 추가
  @Transactional
  public SubmissionAttachment fileUpload(UUID submissionId, MultipartFile file) {
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
    submissionAttachmentRepository.save(result);
    return result;
  }

  // 첨부 링크 추가
  @Transactional
  public SubmissionAttachment linkUpload(UUID submissionId, SubmissionAttachmentUrlDto dto) {
    Submission submission = submissionCommandService.findByIdOrThrow(submissionId);

    SubmissionAttachment submissionAttachment = SubmissionAttachment.builder()
            .submission(submission)
            .type(FileType.URL)
            .value(dto.url())
            .build();

    submissionAttachmentRepository.save(submissionAttachment);
    return submissionAttachment;
  }

  // 첨부 파일 삭제
  @Transactional
  public void deleteSubmissionAttachment(UUID submissionAttachmentId) {
    SubmissionAttachment submissionAttachment = submissionCommandService.findSubmissionAttachmentByIdOrThrow(submissionAttachmentId);
    if(submissionAttachment.getType() == FileType.FILE){
      fileService.deleteFile(submissionAttachment.getValue());
    }
    submissionAttachmentRepository.delete(submissionAttachment);
  }
}