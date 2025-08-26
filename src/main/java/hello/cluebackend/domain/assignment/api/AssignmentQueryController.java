package hello.cluebackend.domain.assignment.api;

import hello.cluebackend.domain.assignment.api.dto.request.AssignmentAttachmentDto;
import hello.cluebackend.domain.assignment.api.dto.request.CreateAssignmentDto;
import hello.cluebackend.domain.assignment.api.dto.request.ModifyAssignmentDto;
import hello.cluebackend.domain.assignment.application.AssignmentQueryService;
import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.submission.application.SubmissionQueryService;
import hello.cluebackend.global.common.annotation.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@Slf4j
public class AssignmentQueryController {
  private final AssignmentQueryService assignmentQueryService;
  private final SubmissionQueryService submissionQueryService;

  // 과제 생성
  @PostMapping
  public ResponseEntity<Long> createAssignment(
          @CurrentUser Long userId,
          @Valid @RequestBody CreateAssignmentDto request
  ) {
    Assignment assignment = assignmentQueryService.save(request.classId(), request);

    submissionQueryService.assignToAllStudentsInClassroom(request.classId(), assignment);
    return ResponseEntity.ok(assignment.getAssignmentId());
  }

  // 과제 삭제
  @DeleteMapping("/{assignmentId}")
  public ResponseEntity<?> deleteAssignment(
          @CurrentUser Long userId,
          @PathVariable Long assignmentId){
    assignmentQueryService.delete(assignmentId);
    return ResponseEntity.ok("과제를 성공적으로 삭제했습니다.");
  }

  // 과제 수정
  @PatchMapping("/{assignmentId}")
  public ResponseEntity<?> modifyAssignment(
          @CurrentUser Long userId,
          @PathVariable Long assignmentId,
          @Valid @RequestBody ModifyAssignmentDto assignmentDto
  ) {
    Assignment assignment = assignmentQueryService.patchAssignment(assignmentId, assignmentDto);
    return ResponseEntity.ok(assignment);
  }

  // 첨부 파일 추가
  @PostMapping("/{assignmentId}/file")
  public ResponseEntity<?> uploadAttachments(
          @CurrentUser Long userId,
          @PathVariable Long assignmentId,
          @RequestParam("files") MultipartFile[] files
  ) throws IOException {

    for (MultipartFile file : files) {
      assignmentQueryService.uploadFileAttachment(assignmentId, file);
    }

    return ResponseEntity.ok("과제 업로드가 성공했습니다.");
  }

  // 첨부 링크 추가
  @PostMapping("/{assignmentId}/link")
  public ResponseEntity<?> urlAttachments(
          @CurrentUser Long userId,
          @PathVariable Long assignmentId,
          @RequestBody List<AssignmentAttachmentDto> assignmentAttachmentDto
  ){
    assignmentQueryService.uploadUrlAttachment(assignmentId, assignmentAttachmentDto);
    return ResponseEntity.ok("과제 링크가 성공적으로 업로드 되었습니다.");
  }

  // 첨부파일 혹은 링크 삭제
  @DeleteMapping("/attachment/{attachmentId}")
  public ResponseEntity<?> deleteAttachment(
          @CurrentUser Long userId,
          @PathVariable Long attachmentId
  ) {
    assignmentQueryService.deleteAttachment(attachmentId);
    return ResponseEntity.ok("과제를 성공적으로 삭제했습니다.");
  }
}