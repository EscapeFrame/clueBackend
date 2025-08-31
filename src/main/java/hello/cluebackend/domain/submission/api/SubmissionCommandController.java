package hello.cluebackend.domain.submission.api;

import hello.cluebackend.domain.assignment.api.dto.response.SubmissionCheck;
import hello.cluebackend.domain.submission.api.dto.response.SubmissionDto;
import hello.cluebackend.domain.submission.application.SubmissionCommandService;
import hello.cluebackend.domain.submission.domain.SubmissionAttachment;
import hello.cluebackend.global.common.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@Slf4j
public class SubmissionCommandController {
  private final SubmissionCommandService submissionCommandService;

  // 할당된 과제 전체 조회
  @GetMapping("/assignment/{assignmentId}")
  public ResponseEntity<List<SubmissionDto>> findAllSubmission(
          @CurrentUser Long userId,
          @PathVariable Long assignmentId
  ) {
    List<SubmissionDto> result = submissionCommandService.findAllByAssignmentId(userId,assignmentId);
    return ResponseEntity.ok(result);
  }

  // 할당된 과제 단일 조회
  @GetMapping("/{submissionId}")
  public ResponseEntity<SubmissionDto> findSubmission(
          @CurrentUser Long userId,
          @PathVariable Long assignmentId
  ) {
    SubmissionDto result = submissionCommandService.findByAssignmentId(assignmentId);

    return ResponseEntity.ok(result);
  }

  // 전체 학생 과제 제출 여부
  @GetMapping("/{assignmentId}/check")
  public ResponseEntity<List<SubmissionCheck>> checkAssignment(
          @CurrentUser Long userId,
          @PathVariable Long assignmentId
  ){
    List<SubmissionCheck> assignmentChecks = submissionCommandService.checkAssignment(userId,assignmentId);
    return ResponseEntity.ok(assignmentChecks);
  }

  // 첨부파일 다운로드
  @GetMapping("/{submissionAttachmentId}/download")
  public ResponseEntity<Resource> submissionAttachmentDownload(
          @CurrentUser Long userId,
          @PathVariable Long submissionAttachmentId
  ) throws IOException {
    SubmissionAttachment submissionAttachment = submissionCommandService.findsubmissionAttachmentByIdOrThrow(submissionAttachmentId);
    Resource resource = submissionCommandService.downloadAttachment(submissionAttachment);
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + submissionAttachment.getOriginalFileName() + submissionAttachment.getContentType() + "\"")
            .contentType(MediaType.ALL)
            .body(resource);
  }
}
