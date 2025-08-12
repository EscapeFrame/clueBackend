package hello.cluebackend.domain.assignment.management.api;

import hello.cluebackend.domain.assignment.management.api.dto.response.GetAllAssignmentResponse;
import hello.cluebackend.domain.assignment.management.api.dto.response.GetAllAssignmentResponseResult;
import hello.cluebackend.domain.assignment.management.application.AssignmentCommandService;
import hello.cluebackend.domain.assignment.participation.application.SubmissionCommandService;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assignment")
@RequiredArgsConstructor
@Slf4j
public class AssignmentManagementCommandController {
  private final JWTUtil jwtUtil;
  private final AssignmentCommandService assignmentCommandService;
  private final UserRepository userRepository;
  private final SubmissionCommandService submissionCommandService;

  // 사용자가 속한 모든 수업 과제 조회
  @GetMapping("/assignments/me")
  public ResponseEntity<GetAllAssignmentResponseResult> getAllAssignments(HttpServletRequest request) {
    String token = jwtUtil.getToken(request);
    Long userId = jwtUtil.getUserId(token);
    List<GetAllAssignmentResponse> result = assignmentCommandService.findAllAssignment(userId);
    return ResponseEntity.ok(new GetAllAssignmentResponseResult(result));
  }

  //
//  @GetMapping("/getAllAssignments/{classId}")
//  public ResponseEntity<GetAllClassRoomAssignmentResponseResult> getAllClassroomAssignment(HttpServletRequest request, @PathVariable Long classId) {
//    try{
//      String token = jwtUtil.getToken(request);
//      Long userId = jwtUtil.getUserId(token);
//      return ResponseEntity.ok(new GetAllClassRoomAssignmentResponseResult(assignmentCommandService.findAllClassroomAssignment(classId)));
//    }catch (Exception e){
//      log.error("Failed to retrieve assignments in classroom for user", e);
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//  }

  // TODO : 특정 과제 학생 제출 여부 전체 조회, 학습실 - 과제 - 확인(T)
//  @GetMapping("/checkAssignment/{assignmentId}")
//  public ResponseEntity<GetAllSubmissionCheckResult> getAllSubmissionCheck(HttpServletRequest request, @PathVariable Long assignmentId) {
//    try {
//      String token = jwtUtil.getToken(request);
//      Long userId = jwtUtil.getUserId(token);
//
//      Assignment assignment = assignmentCommandService.findById(assignmentId);
//      return ResponseEntity.ok(new GetAllSubmissionCheckResult(assignmentCommandService.checkIsSubmitted(assignment);));
//    }catch (Exception e){
//      log.error("Failed to retrieve assignments in classroom for user", e);
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//  }

  // TODO : 개인 제출 과제 보기, 학습실 - 과제 - 채점(T)
//  @GetMapping("/getAssignment/{submissionId}")
//  public ResponseEntity<GetSubmissionResult> getSubmission(HttpServletRequest request, @PathVariable Long submissionId) {
//    try {
//      String token = jwtUtil.getToken(request);
//      Long userId = jwtUtil.getUserId(token);
//
//      Submission submission = submissionCommandService.findById(submissionId);
//      GetSubmissionFile result = submissionCommandService.getSubmissionGrade(submission);
//
//      return ResponseEntity.ok(new GetSubmissionResult(result));
//    }catch (Exception e){
//      log.error("Failed to retrieve assignments in classroom for user", e);
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//  }
}
