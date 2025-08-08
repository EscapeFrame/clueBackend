package hello.cluebackend.assignment.management.api;

import hello.cluebackend.assignment.management.api.dto.response.GetAllAssignmentResponse;
import hello.cluebackend.assignment.management.api.dto.response.GetAllAssignmentResponseResult;
import hello.cluebackend.assignment.management.application.command.AssignmentManagementCommandService;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import hello.cluebackend.domain.user.service.UserService;
import hello.cluebackend.global.config.JWTUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assignment/teacher")
@RequiredArgsConstructor
@Slf4j
public class AssignmentManagementCommandController {
  private final JWTUtil jwtUtil;
  private final UserService userService;
  private final AssignmentManagementCommandService assignmentManagementCommandService;
  private final UserRepository userRepository;

  // TODO : 메인 페이지 제작한 과제 전체 조회
  @GetMapping("/getAllAssignments")
  public ResponseEntity<GetAllAssignmentResponseResult> getAllAssignments(HttpServletRequest request){
    try {
      String token = jwtUtil.getToken(request);
      Long userId = jwtUtil.getUserId(token);

      UserEntity user = userRepository.findById(userId)
              .orElseThrow(() -> new UsernameNotFoundException("User not found."));

      List<GetAllAssignmentResponse> assignments = assignmentManagementCommandService.findAllAssignment(user);

      return ResponseEntity.ok(new GetAllAssignmentResponseResult(assignments));

    } catch (JwtException | UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  // TODO : 학습실 과제 전체 조회, 학습실 - 과제(T)
  @GetMapping("/getAllAssignments/{classId}")
  public ResponseEntity<>
  // TODO : 특정 과제 학생 제출 여부 전체 조회, 학습실 - 과제 - 확인(T)
  //@GetMapping("{classId}/checkAssignment/{assignmentId}")

  // TODO : 개인 제출 과제 보기, 학습실 - 과제 - 채점(T)
  //@GetMapping("/{classId}/{assignmentId}/getAssignment/{studentId}")

}
