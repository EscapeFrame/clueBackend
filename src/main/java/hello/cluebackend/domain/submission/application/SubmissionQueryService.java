package hello.cluebackend.domain.submission.application;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.submission.api.dto.request.SubmissionAttachmentUrlDto;
import hello.cluebackend.domain.submission.domain.fileType;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.submission.domain.Submission;
import hello.cluebackend.domain.submission.domain.SubmissionAttachment;
import hello.cluebackend.domain.submission.persistence.SubmissionRepository;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.domain.classroomuser.application.ClassroomUserService;
import hello.cluebackend.domain.submission.persistence.SubmissionAttachmentRepository;
import hello.cluebackend.domain.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
  public void assignToAllStudentsInClassroom(Long classroomId, Assignment assignment){
    ClassRoom classRoom = classRoomService.findById(classroomId).toEntity();
    List<UserEntity> users = classroomUserService.findAllClassroomUser(classRoom);
    List<Submission> submissions = users.stream()
            .map(u -> new Submission(assignment, u, false, null))
            .toList();
    submissionRepository.saveAll(submissions);
  }

  // 과제 제출하기
  @Transactional
  public Submission submitSubmission(Long submissionId) {
    Submission submission = submissionCommandService.findByIdOrThrow(submissionId);
    submission.submit();
    return submissionRepository.save(submission);
  }

  // 과제 제출 취소하기
  @Transactional
  public Submission cancelSubmission(Long submissionId) {
    Submission submission = submissionCommandService.findByIdOrThrow(submissionId);
    submission.cancel();
    return submissionRepository.save(submission);
  }


  // 첨부 파일 추가
  @Transactional
  public SubmissionAttachment fileUpload(Long submissionId, MultipartFile file) {
    Submission submission = submissionCommandService.findByIdOrThrow(submissionId);

    String storedFiledName = fileService.storeFile(file);

    SubmissionAttachment result = SubmissionAttachment.builder()
            .submission(submission)
            .type(fileType.file)
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
  public SubmissionAttachment linkUpload(Long submissionId, SubmissionAttachmentUrlDto dto) {
    Submission submission = submissionCommandService.findByIdOrThrow(submissionId);

    SubmissionAttachment submissionAttachment = SubmissionAttachment.builder()
            .submission(submission)
            .type(fileType.url)
            .value(dto.url())
            .build();

    submissionAttachmentRepository.save(submissionAttachment);
    return submissionAttachment;
  }

  // 첨부 파일 삭제
  @Transactional
  public void deleteSubmissionAttachment(Long submissionAttachmentId) {
    SubmissionAttachment submissionAttachment = submissionCommandService.findAssignmentAttachmentByIdOrThrow(submissionAttachmentId);
    if(submissionAttachment.getType() == fileType.file){
      fileService.deleteFile(submissionAttachment.getValue());
    }
    submissionAttachmentRepository.delete(submissionAttachment);
  }
}