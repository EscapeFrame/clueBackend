package hello.cluebackend.domain.submission.api;

import hello.cluebackend.domain.submission.application.SubmissionQueryService;
import hello.cluebackend.global.common.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@Slf4j
public class SubmissionQueryController {
  private final SubmissionQueryService submissionQueryService;

  // 과제 제출 하기
  @PatchMapping("/{submissionId}/submit")
  public ResponseEntity<?> submitSubmission(
          @CurrentUser Long userId,
          @PathVariable Long submissionId
    ) {
    submissionQueryService.submitSubmission(submissionId);
    return ResponseEntity.ok("성공적으로 과제가 제출되었습니다.");
  }

  // 과제 제출 취소하기
  @PatchMapping("/{submissionId}/cancel")
  public ResponseEntity<?> cancelSubmission(
          @CurrentUser Long userId,
          @PathVariable Long submissionId
  ){
    submissionQueryService.cancelSubmission(submissionId);
    return ResponseEntity.ok("과제 제출이 취소되었습니다.");
  }

  // TODO : 과제 파일 업로드 하기
//  @PostMapping("/{submissionId}/upload")
//  public ResponseEntity<?> fileUpload(
//          @CurrentUser Long userId,
//          @PathVariable Long submissionId,
//          @RequestBody MultipartFile file
//          ) {
//    submissionQueryService.fileUpload(submissionId, file);
//    return ResponseEntity.ok("첨부파일 업로드가 성공적으로 이뤄졌습니다.");
//  }

  // TODO : 과제 첨부 파일 삭제하기
//  @DeleteMapping("/{submissionAttachmentId}/delete")
//  public ResponseEntity<?> deleteFile(
//          @PathVariable Long submissionAttachmentId
//  ) {
//    submissionQueryService.deletefile(submissionAttachmentId);
//    return ResponseEntity.ok("첨부 파일 삭제");
//  }
}