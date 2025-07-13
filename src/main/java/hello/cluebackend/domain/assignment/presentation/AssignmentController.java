package hello.cluebackend.domain.assignment.presentation;


import hello.cluebackend.domain.assignment.presentation.dto.request.AssignmentCreateRequestDto;
import hello.cluebackend.domain.assignment.service.AssignmentService;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assignments")
public class AssignmentController {
  private final AssignmentService assignmentService;
  private final JWTUtil jWTUtil;

  @PostMapping("/{classId}")
  public ResponseEntity<String> createAssignment(
          HttpServletRequest request,
          @PathVariable Long classId,
          @RequestBody AssignmentCreateRequestDto requestDto
          ){
    String token = jWTUtil.getToken(request);
    Long userId = jWTUtil.getUserId(token);
    return ResponseEntity.ok(assignmentService.createAssignment(userId, classId, requestDto));



  }
//  @GetMapping("{classId}")
//  public ResponseEntity<List<AssignmentCardDto>> getAllAssignments(
//          @PathVariable Long classId,
//          @RequestParam Long userId
//  ){
//    return ResponseEntity.ok(assignmentService.getAssignmentsForStudent(classId, userId));
//  }
}
