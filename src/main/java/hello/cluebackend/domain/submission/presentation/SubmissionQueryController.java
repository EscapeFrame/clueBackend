package hello.cluebackend.domain.submission.presentation;

import hello.cluebackend.domain.submission.presentation.dto.request.SubmissionAttachmentUrlDto;
import hello.cluebackend.domain.submission.presentation.dto.response.SubmissionDto;
import hello.cluebackend.domain.submission.application.SubmissionQueryService;
import hello.cluebackend.domain.submission.domain.Submission;
import hello.cluebackend.domain.submission.domain.SubmissionAttachment;
import hello.cluebackend.global.common.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@Slf4j
public class SubmissionQueryController {
  private final SubmissionQueryService submissionQueryService;

  // 과제 제출 하기
  @PatchMapping("/{submissionId}/submit")
  public ResponseEntity<SubmissionDto> submitSubmission(
          @CurrentUser UUID userId,
          @PathVariable UUID submissionId
    ) {
    Submission submission = submissionQueryService.submitSubmission(submissionId);
    return ResponseEntity.ok(SubmissionDto.from(submission));
  }

  // 과제 제출 취소하기
  @PatchMapping("/{submissionId}/cancel")
  public ResponseEntity<SubmissionDto> cancelSubmission(
          @CurrentUser UUID userId,
          @PathVariable UUID submissionId
  ){
    Submission submission = submissionQueryService.cancelSubmission(submissionId);
    return ResponseEntity.ok(SubmissionDto.from(submission));
  }

  // 과제 첨부파일 삭제하기
  @DeleteMapping("/{submissionAttachmentId}")
  public ResponseEntity<?> deleteSubmissionAttachment(
          @CurrentUser UUID userId,
          @PathVariable UUID submissionAttachmentId
  ){
    submissionQueryService.deleteSubmissionAttachment(submissionAttachmentId);
    return ResponseEntity.ok("과제 첨부파일이 성공적으로 삭제되었습니다.");
  }

  // 과제 제출 첨부 파일 추가
  @PostMapping("/{submissionId}/file")
  public ResponseEntity<SubmissionAttachment> fileUpload(
          @CurrentUser UUID userId,
          @PathVariable UUID submissionId,
          @RequestBody MultipartFile file
  ) throws IOException {
    SubmissionAttachment submissionAttachment = submissionQueryService.fileUpload(submissionId, file);
    return ResponseEntity.ok(submissionAttachment);
  }

  // 과제 제출 첨부 링크 추가
  @PostMapping("/{submisisonId}/link")
  public ResponseEntity<?> linkUpload(
          @CurrentUser UUID userId,
          @PathVariable UUID submissionId,
          @RequestBody SubmissionAttachmentUrlDto dto
  ) {
    submissionQueryService.linkUpload(submissionId, dto);
    return ResponseEntity.ok("첨부 파일 삭제");
  }
}