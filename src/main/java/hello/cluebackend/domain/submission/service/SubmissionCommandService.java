package hello.cluebackend.domain.submission.service;

import hello.cluebackend.application.classroom.mapper.ClassRoomMapper;
import hello.cluebackend.domain.assignment.exception.AccessDeniedException;
import hello.cluebackend.domain.assignment.service.AssignmentCommandService;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.classroom.service.ClassRoomQueryService;
import hello.cluebackend.domain.assignment.model.Assignment;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.application.submission.dto.response.SubmissionAttachmentResponse;
import hello.cluebackend.application.submission.dto.response.SubmissionCheck;
import hello.cluebackend.application.submission.dto.response.SubmissionResponse;
import hello.cluebackend.domain.submission.model.Submission;
import hello.cluebackend.domain.submission.model.SubmissionAttachment;
import hello.cluebackend.infrastructure.persistence.submission.SubmissionJpaRepository;
import hello.cluebackend.infrastructure.persistence.submissionattachment.SubmissionAttachmentJpaRepository;
import hello.cluebackend.domain.user.model.Role;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.domain.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmissionCommandService {
  private final SubmissionJpaRepository submissionJpaRepository;
  private final SubmissionAttachmentJpaRepository submissionAttachmentJpaRepository;
  private final AssignmentCommandService assignmentCommandService;
  private final ClassRoomQueryService classRoomQueryService;
  private final FileService fileService;
  private final UserService userService;
  private final ClassRoomMapper classRoomMapper;

  // 과제 전체 조회 및 과제 첨부 파일 조회 (downloadUrl 포함)
  public List<SubmissionResponse> findAllByAssignmentId(UUID userId, UUID classId) {
    UserEntity user = userService.findById(userId);
    ClassRoom classRoom = classRoomMapper.fromClassRoomDtoToEntity(classRoomQueryService.findById(userId, classId));

    List<Submission> submissions = submissionJpaRepository.findAllByClassRoomAndUser(classRoom, user);

    return submissions.stream()
            .map(submission -> {
              List<SubmissionAttachmentResponse> submissionAttachmentResponses =
                      submissionAttachmentJpaRepository.findAllBySubmission(submission).stream()
                              .map(attachment -> {
                                String downloadUrl = getDownloadUrlForAttachment(attachment);
                                return SubmissionAttachmentResponse.from(attachment, downloadUrl);
                              })
                              .toList();

              return SubmissionResponse.from(submission, submissionAttachmentResponses);
            })
            .toList();
  }

  // 과제 제출 단일 조회 (downloadUrl 포함)
  public SubmissionResponse findByAssignmentId(UUID userId, UUID submissionId) {
    Submission submission = findByIdOrThrow(submissionId);
    UserEntity requestUser = userService.findById(userId);

    if (!submission.getUser().getUserId().equals(userId) && !requestUser.getRole().equals(Role.TEACHER)) {
      throw new AccessDeniedException("사용자가 제출한 과제가 아닙니다.");
    }

    List<SubmissionAttachment> submissionAttachments = submissionAttachmentJpaRepository.findAllBySubmission(submission);
    List<SubmissionAttachmentResponse> submissionAttachmentResponses = submissionAttachments.stream()
            .map(attachment -> {
              String downloadUrl = getDownloadUrlForAttachment(attachment);
              return SubmissionAttachmentResponse.from(attachment, downloadUrl);
            })
            .toList();
    return SubmissionResponse.from(submission, submissionAttachmentResponses);
  }

  // 전체 학생 과제 제출 여부 (선생)
  public List<SubmissionCheck> checkAssignment(UUID userId, UUID assignmentId) {
    UserEntity requestUser = userService.findById(userId);
    if (!requestUser.getRole().equals(Role.TEACHER)) {
      throw new AccessDeniedException("선생님만 조회할 수 있습니다.");
    }

    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);
    List<Submission> submissions = submissionJpaRepository.findAllByAssignment(assignment);
    return submissions.stream()
            .filter(s -> s.getUser().getRole()==Role.STUDENT)
            .map(SubmissionCheck::from)
            .toList();
  }

  // 첨부 파일 혹은 링크 전체 조회 (학생) - downloadUrl 포함
  public List<SubmissionAttachmentResponse> findAllAssignmentStudent(UUID userId, UUID submissionId) {
    Submission submission = findByIdOrThrow(submissionId);
    UserEntity requestUser = userService.findById(userId);

    if (!submission.getUser().getUserId().equals(userId) && !requestUser.getRole().equals(Role.TEACHER)) {
      throw new AccessDeniedException("사용자가 제출한 과제가 아닙니다.");
    }

    List<SubmissionAttachment> attachments = submissionAttachmentJpaRepository.findAllBySubmission(submission);
    return attachments.stream()
            .filter(sa -> sa.getUser().getUserId().equals(userId))
            .map(attachment -> {
              String downloadUrl = getDownloadUrlForAttachment(attachment);
              return SubmissionAttachmentResponse.from(attachment, downloadUrl);
            })
            .toList();
  }

  // 첨부 파일 혹은 링크 전체 조회 (선생) - downloadUrl 포함
  public List<SubmissionAttachmentResponse> findAllAssignmentTeacher(UUID userId, UUID submissionId) {
    UserEntity requestUser = userService.findById(userId);
    if (!requestUser.getRole().equals(Role.TEACHER)) {
      throw new AccessDeniedException("선생님만 조회할 수 있습니다.");
    }

    Submission submission = findByIdOrThrow(submissionId);
    List<SubmissionAttachment> attachments = submissionAttachmentJpaRepository.findAllBySubmission(submission);
    return attachments.stream()
            .map(attachment -> {
              String downloadUrl = getDownloadUrlForAttachment(attachment);
              return SubmissionAttachmentResponse.from(attachment, downloadUrl);
            })
            .toList();
  }

  // S3 다운로드 URL 생성 함수
  public String getAttachmentDownloadUrl(UUID submissionAttachmentId) {
    SubmissionAttachment attachment = findSubmissionAttachmentByIdOrThrow(submissionAttachmentId);
    return getDownloadUrlForAttachment(attachment);
  }

  //
  private String getDownloadUrlForAttachment(SubmissionAttachment attachment) {
    if (attachment.getType() == hello.cluebackend.domain.submission.model.FileType.FILE) {
      return fileService.getPresignedDownloadUrl(attachment.getValue());
    }
    return attachment.getValue();
  }

  public Submission findByIdOrThrow(UUID submissionId) {
    return submissionJpaRepository.findById(submissionId)
            .orElseThrow(() -> new EntityNotFoundException("해당 제출 과제를 찾을수 없습니다."));
  }

  public SubmissionAttachment findSubmissionAttachmentByIdOrThrow(UUID submissionAttachmentId) {

    return submissionAttachmentJpaRepository.findById(submissionAttachmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 과제 제출 첨부파일을 찾을수 없습니다."));
  }
}
