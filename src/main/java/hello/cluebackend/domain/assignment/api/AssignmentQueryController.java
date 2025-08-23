package hello.cluebackend.domain.assignment.api;

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

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@Slf4j
public class AssignmentQueryController {
  private final AssignmentQueryService assignmentService;
  private final SubmissionQueryService submissionQueryService;

  // 과제 생성
  @PostMapping("")
  public ResponseEntity<Assignment> createAssignment(@CurrentUser Long userId, @Valid @RequestBody CreateAssignmentDto request){
    Assignment assignment = assignmentService.save(request.classId(), request);
    submissionQueryService.assignToAllStudentsInClassroom(request.classId(), assignment);
    return ResponseEntity.ok(assignment);
  }

  // 과제 삭제
  @DeleteMapping("/{assignmentId}")
  public ResponseEntity<?> deleteAssignment(
          @CurrentUser Long userId,
          @PathVariable Long assignmentId){
    assignmentService.delete(assignmentId);
    return ResponseEntity.ok("과제를 성공적으로 삭제했습니다.");
  }

  // 과제 수정
  @PatchMapping("/{assignmentId}")
  public ResponseEntity<?> modifyAssignment(
          @CurrentUser Long userId,
          @PathVariable Long assignmentId,
          @Valid @RequestBody ModifyAssignmentDto assignmentDto
  ) {
    assignmentService.patchAssignment(assignmentId, assignmentDto);
    return ResponseEntity.ok("과제를 성공적으로 수정했습니다.");
  }

  // 첨부파일 생성
//  @PostMapping("/upload")
//  public ResponseEntity<?> uploadAttachment(@CurrentUser Long userId, @RequestBody UploadAttachment request, @RequestParam("file") MultipartFile file
//  ) throws IOException {
//    submissionQueryService.uploadAttachment(request,file);
//    return ResponseEntity.ok("과제 업로드가 성공했습니다.");
//  }

  // 첨부파일 삭제
//  @DeleteMapping("/deleteAttachment/{attachmentId}")
//  public ResponseEntity<?> deleteAttachment(@CurrentUser Long userId, @PathVariable Long attachmentId, @RequestParam("file") MultipartFile file) throws IOException {
//    String filePath = "uploads/" + file.getOriginalFilename();
//    submissionQueryService.deleteAttachment()
//    return ResponseEntity.ok("과제를 성공적으로 삭제했습니다.");
//  }
}
