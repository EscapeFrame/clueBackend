package hello.cluebackend.domain.assignment.api;

import hello.cluebackend.domain.assignment.api.dto.response.AssignmentCheck;
import hello.cluebackend.domain.assignment.api.dto.response.AssignmentDto;
import hello.cluebackend.domain.assignment.api.dto.response.GetAllAssignmentDto;
import hello.cluebackend.domain.assignment.application.AssignmentCommandService;
import hello.cluebackend.domain.submission.application.SubmissionCommandService;
import hello.cluebackend.global.common.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@Slf4j
public class AssignmentCommandController {
  private final AssignmentCommandService assignmentCommandService;
  private final SubmissionCommandService submissionCommandService;

  // 사용자가 속한 모든 수업 과제 조회
  @GetMapping("/me")
  public ResponseEntity<List<GetAllAssignmentDto>> getAllAssignments(@CurrentUser Long userId) {
    List<GetAllAssignmentDto> result = assignmentCommandService.findAllAssignment(userId);
    return ResponseEntity.ok(result);
  }

  // 과제 모든 정보 조회
  @GetMapping("/{assignmentId}")
  public ResponseEntity<AssignmentDto> getAssignment(@CurrentUser Long userId, @PathVariable Long assignmentId) {
    AssignmentDto result = assignmentCommandService.findById(assignmentId);
    return ResponseEntity.ok(result);
  }

  // 전체 학생 과제 제출 여부
  @GetMapping("/{assignmentId}/check")
  public ResponseEntity<List<AssignmentCheck>> checkAssignment(@CurrentUser Long userId, @PathVariable Long assignmentId){
    List<AssignmentCheck> assignmentChecks = submissionCommandService.checkAssignment(assignmentId);
    return ResponseEntity.ok(assignmentChecks);
  }
}
