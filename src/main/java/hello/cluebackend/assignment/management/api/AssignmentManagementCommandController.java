package hello.cluebackend.assignment.management.api;

import hello.cluebackend.assignment.management.api.dto.response.*;
import hello.cluebackend.assignment.management.application.AssignmentCommandService;
import hello.cluebackend.assignment.management.domain.Assignment;
import hello.cluebackend.assignment.participation.application.SubmissionCommandService;
import hello.cluebackend.assignment.participation.domain.Submission;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import hello.cluebackend.global.config.JWTUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assignment/teacher")
@RequiredArgsConstructor
@Slf4j
public class AssignmentManagementCommandController {
  private final JWTUtil jwtUtil;
  private final AssignmentCommandService assignmentCommandService;
  private final UserRepository userRepository;
  private final SubmissionCommandService submissionCommandService;

  // TODO : 메인 페이지 제작한 과제 전체 조회
  @GetMapping("/getAllAssignments")
  public ResponseEntity<AssignmentResult> getAllAssignments(HttpServletRequest request) {
    try {
      String token = jwtUtil.getToken(request);
      Long userId = jwtUtil.getUserId(token);

      UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found."));
      List<GetAllAssignmentResponse> assignments = assignmentCommandService.findAllAssignment(user);

      return ResponseEntity.ok(new AssignmentResult(assignments));
    } catch (JwtException | UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  // TODO : 학습실 과제 전체 조회, 학습실 - 과제(T)
  @GetMapping("/getAllAssignments/{classId}")
  public ResponseEntity<AssignmentResult> getAllClassroomAssignment(HttpServletRequest request, @PathVariable Long classId) {
    try{
      String token = jwtUtil.getToken(request);
      Long userId = jwtUtil.getUserId(token);

      List<GetAllClassRoomAssignmentResponse> result = assignmentCommandService.findAllClassroomAssignment(classId);

      return ResponseEntity.ok(new AssignmentResult(result));
    }catch(JwtException | UsernameNotFoundException e){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  // TODO : 특정 과제 학생 제출 여부 전체 조회, 학습실 - 과제 - 확인(T)
  @GetMapping("/checkAssignment/{assignmentId}")
  public ResponseEntity<AssignmentResult> getAllSubmissionCheck(HttpServletRequest request, @PathVariable Long assignmentId) {
    try {
      String token = jwtUtil.getToken(request);
      Long userId = jwtUtil.getUserId(token);

      Assignment assignment = assignmentCommandService.findById(assignmentId);

      List<GetAllSubmissionCheck> result = assignmentCommandService.checkIsSubmitted(assignment);

      return ResponseEntity.ok(new AssignmentResult(result));
    }catch (JwtException | UsernameNotFoundException e){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

//  // TODO : 개인 제출 과제 보기, 학습실 - 과제 - 채점(T)
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
//    }catch (JwtException | UsernameNotFoundException e){
//      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }catch (Exception e){
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//  }
}
