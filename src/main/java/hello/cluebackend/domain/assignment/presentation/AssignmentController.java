package hello.cluebackend.domain.assignment.presentation;

import hello.cluebackend.domain.assignment.presentation.dto.request.CreateAssignmentRequestDto;
import hello.cluebackend.domain.assignment.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assignment")
public class AssignmentController {
  private final AssignmentService assignmentService;

  // 과제 생성 ( 선생님 )
  @PostMapping("/")
  public String createAssignment(
          @RequestParam int class_id,
          @RequestBody CreateAssignmentRequestDto request,
          Authentication authentication
  ){
    assignmentService.createAssignment(class_id, request, authentication);
    return null;
  }

  // 과제 전체 조회 ( 선생님 )
}
