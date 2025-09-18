package hello.cluebackend.domain.assignment.presentation;

import hello.cluebackend.domain.assignment.presentation.dto.request.AssignmentAttachmentDto;
import hello.cluebackend.domain.assignment.presentation.dto.request.CreateAssignmentDto;
import hello.cluebackend.domain.assignment.presentation.dto.request.ModifyAssignmentDto;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@Slf4j
public class AssignmentQueryController {
  private final AssignmentQueryService assignmentQueryService;
  private final SubmissionQueryService submissionQueryService;

  // 과제 생성
  @PostMapping
  public ResponseEntity<UUID> createAssignment(
          @CurrentUser UUID userId,
          @Valid @RequestBody CreateAssignmentDto request
  ) {
    Assignment assignment = assignmentQueryService.save(userId, request);

    submissionQueryService.assignToAllStudentsInClassroom(request.classId(), assignment);
    return ResponseEntity.ok(assignment.getAssignmentId());
  }

  // 과제 삭제
  @DeleteMapping("/{assignmentId}")
  public ResponseEntity<?> deleteAssignment(
          @CurrentUser UUID userId,
          @PathVariable UUID assignmentId){
    assignmentQueryService.delete(assignmentId);
    return ResponseEntity.ok("과제를 성공적으로 삭제했습니다.");
  }

  // 과제 수정
  @PatchMapping("/{assignmentId}")
  public ResponseEntity<?> modifyAssignment(
          @CurrentUser UUID userId,
          @PathVariable UUID assignmentId,
          @Valid @RequestBody ModifyAssignmentDto assignmentDto
  ) {
    UUID assignment = assignmentQueryService.patchAssignment(assignmentId, assignmentDto);

    return ResponseEntity.ok(assignment);
  }

  // 첨부 파일 추가
  @PostMapping("/{assignmentId}/file")
  public ResponseEntity<?> uploadAttachments(
          @CurrentUser UUID userId,
          @PathVariable UUID assignmentId,
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
          @CurrentUser UUID userId,
          @PathVariable UUID assignmentId,
          @RequestBody List<AssignmentAttachmentDto> assignmentAttachmentDto
  ){
    assignmentQueryService.uploadUrlAttachment(assignmentId, assignmentAttachmentDto);
    return ResponseEntity.ok("과제 링크가 성공적으로 업로드 되었습니다.");
  }

  // 첨부파일 혹은 링크 삭제
  @DeleteMapping("/attachment/{attachmentId}")
  public ResponseEntity<?> deleteAttachment(
          @CurrentUser UUID userId,
          @PathVariable UUID attachmentId
  ) {
    assignmentQueryService.deleteAttachment(attachmentId);
    return ResponseEntity.ok("과제를 성공적으로 삭제했습니다.");
  }
}