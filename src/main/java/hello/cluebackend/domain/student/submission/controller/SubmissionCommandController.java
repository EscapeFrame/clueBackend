package hello.cluebackend.domain.student.submission.controller;

import hello.cluebackend.domain.student.assignment.exception.AccessDeniedException;
import hello.cluebackend.domain.student.classroomuser.service.ClassroomUserService;
import hello.cluebackend.domain.student.submission.service.SubmissionCommandService;
import hello.cluebackend.domain.student.submission.domain.SubmissionAttachment;
import hello.cluebackend.domain.student.submission.controller.dto.response.SubmissionCheck;
import hello.cluebackend.domain.student.submission.controller.dto.response.SubmissionResponse;
import hello.cluebackend.global.common.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import hello.cluebackend.domain.submission.presentation.dto.response.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@Slf4j
public class SubmissionCommandController {
  private final SubmissionCommandService submissionCommandService;
  private final ClassroomUserService classroomUserService;

  // 할당된 과제 전체 조회
  @GetMapping("/{classId}")
  public ResponseEntity<List<SubmissionResponse>> findSubmission(
          @CurrentUser UUID userId,
          @PathVariable UUID classId
  ) {
    if (!classroomUserService.isUserInClassroom(classId, userId)) {
      throw new AccessDeniedException("해당 수업실에 속하지 않은 유저입니다.");
    }
    List<SubmissionResponse> result = submissionCommandService.findAllByAssignmentId(userId,classId);
    return ResponseEntity.ok(result);
  }

  // 할당된 과제 조회
  @GetMapping("/assignment/{submissionId}")
  public ResponseEntity<SubmissionResponse> findAllSubmission(
          @CurrentUser UUID userId,
          @PathVariable UUID submissionId
  ) {
    SubmissionResponse result = submissionCommandService.findByAssignmentId(userId, submissionId);
    return ResponseEntity.ok(result);
  }

  // 전체 학생 과제 제출 여부
  @GetMapping("/{assignmentId}/check")
  public ResponseEntity<List<SubmissionCheck>> checkAssignment(
          @CurrentUser UUID userId,
          @PathVariable UUID assignmentId
  ){
    List<SubmissionCheck> assignmentChecks = submissionCommandService.checkAssignment(userId,assignmentId);
    return ResponseEntity.ok(assignmentChecks);
  }

  // 첨부파일 다운로드
  @GetMapping("/{submissionAttachmentId}/download")
  public ResponseEntity<Resource> submissionAttachmentDownload(
          @CurrentUser UUID userId,
          @PathVariable UUID submissionAttachmentId
  ) throws IOException {
    SubmissionAttachment submissionAttachment = submissionCommandService.findSubmissionAttachmentByIdOrThrow(submissionAttachmentId);
    Resource resource = submissionCommandService.downloadAttachment(submissionAttachment);

    String original = submissionAttachment.getOriginalFileName();
    String contentType = submissionAttachment.getContentType();
    MediaType mediaType = (contentType != null) ? MediaType.parseMediaType(contentType) : MediaType.ALL;

    return ResponseEntity.ok()
            .contentType(mediaType)
            .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                    .filename(original, StandardCharsets.UTF_8)
                    .build()
                    .toString())
            .body(resource);
  }
}
