package hello.cluebackend.domain.submission.service;

import hello.cluebackend.domain.assignment.exception.AccessDeniedException;
import hello.cluebackend.domain.assignment.service.AssignmentCommandService;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.submission.controller.dto.response.SubmissionAttachmentResponse;
import hello.cluebackend.domain.submission.controller.dto.response.SubmissionCheck;
import hello.cluebackend.domain.submission.controller.dto.response.SubmissionResponse;
import hello.cluebackend.domain.submission.domain.Submission;
import hello.cluebackend.domain.submission.domain.SubmissionAttachment;
import hello.cluebackend.domain.submission.domain.repository.SubmissionRepository;
import hello.cluebackend.domain.submission.domain.repository.SubmissionAttachmentRepository;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.UserEntity;
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
  private final SubmissionRepository submissionRepository;
  private final SubmissionAttachmentRepository submissionAttachmentRepository;
  private final AssignmentCommandService assignmentCommandService;
  private final ClassRoomService classRoomService;
  private final FileService fileService;
  private final UserService userService;

  // 과제 전체 조회 및 과제 첨부 파일 조회
  public List<SubmissionResponse> findAllByAssignmentId(UUID userId, UUID classId) {
    UserEntity user = userService.findById(userId).toEntity();
    ClassRoom classRoom = classRoomService.findById(userId, classId);

    List<Submission> submissions = submissionRepository.findAllByClassRoomAndUser(classRoom, user);

    return submissions.stream()
            .map(submission -> {
              List<SubmissionAttachmentResponse> submissionAttachmentResponses =
                      submissionAttachmentRepository.findAllBySubmission(submission).stream()
                              .map(SubmissionAttachmentResponse::from)
                              .toList();

              return SubmissionResponse.from(submission, submissionAttachmentResponses);
            })
            .toList();
  }

  // 과제 제출 단일 조회
  public SubmissionResponse findByAssignmentId(UUID userId, UUID submissionId) {
    Submission submission = findByIdOrThrow(submissionId);

    if (!submission.getUser().getUserId().equals(userId) && !submission.getUser().getRole().equals(Role.TEACHER)) {
      throw new AccessDeniedException("사용자가 제출한 과제가 아닙니다.");
    }

    List<SubmissionAttachment> submissionAttachments = submissionAttachmentRepository.findAllBySubmission(submission);
    List<SubmissionAttachmentResponse> submissionAttachmentResponses = submissionAttachments.stream()
            .map(SubmissionAttachmentResponse::from)
            .toList();
    return SubmissionResponse.from(submission, submissionAttachmentResponses);
  }

  // 전체 학생 과제 제출 여부 (선생)
  public List<SubmissionCheck> checkAssignment(UUID userId, UUID assignmentId) {
    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);
    List<Submission> submissions = submissionRepository.findAllByAssignment(assignment);
    return submissions.stream()
            .filter(s -> s.getUser().getUserId().equals(userId))
            .map(s -> SubmissionCheck.from(s))
            .toList();
  }

  // 첨부 파일 혹은 링크 전체 조회 (학생)
  public List<SubmissionAttachmentResponse> findAllAssignmentStudent(UUID userId, UUID submissionId) {
    Submission submission = findByIdOrThrow(submissionId);
    List<SubmissionAttachment> attachments = submissionAttachmentRepository.findAllBySubmission(submission);
    return attachments.stream()
            .filter(sa -> sa.getUser().getUserId().equals(userId))
            .map(sa -> SubmissionAttachmentResponse.from(sa))
            .toList();
  }

  // 첨부 파일 혹은 링크 전체 조회 (선생)
  public List<SubmissionAttachmentResponse> findAllAssignmentTeacher(UUID submissionId) {
    Submission submission = findByIdOrThrow(submissionId);
    List<SubmissionAttachment> attachments = submissionAttachmentRepository.findAllBySubmission(submission);
    return attachments.stream()
            .map(sa -> SubmissionAttachmentResponse.from(sa))
            .toList();
  }

  // 첨부파일 다운로드
  public Resource downloadAttachment(SubmissionAttachment submissionAttachment) throws IOException {
    String path = submissionAttachment.getValue();
    return fileService.downloadFile(path);
  }

  public Submission findByIdOrThrow(UUID submissionId) {
    return submissionRepository.findById(submissionId)
            .orElseThrow(() -> new EntityNotFoundException("해당 제출 과제를 찾을수 없습니다."));
  }

  public SubmissionAttachment findSubmissionAttachmentByIdOrThrow(UUID submissionAttachmentId) {

    return submissionAttachmentRepository.findById(submissionAttachmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 과제 제출 첨부파일을 찾을수 없습니다."));
  }
}
