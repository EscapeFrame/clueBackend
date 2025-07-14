package hello.cluebackend.domain.assignment.presentation;


import hello.cluebackend.domain.assignment.presentation.dto.request.AssignmentCreateRequestDto;
import hello.cluebackend.domain.assignment.service.AssignmentService;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assignments")
public class AssignmentController {
  private final AssignmentService assignmentService;
  private final JWTUtil jWTUtil;

  @PostMapping("/{classId}")
  public ResponseEntity<?> createAssignment(
          HttpServletRequest request,
          @PathVariable Long classId,
          @RequestBody AssignmentCreateRequestDto requestDto,
          @RequestPart(value = "file", required = false) MultipartFile file
  ){
    String token = jWTUtil.getToken(request);
    Long userId = jWTUtil.getUserId(token);

    assignmentService.createAssignment(userId, classId, requestDto, file);
    return ResponseEntity.ok(HttpStatus.CREATED);
  }

//  @GetMapping("{classId}")
//  public ResponseEntity<List<AssignmentCardDto>> getAllAssignments(
//          @PathVariable Long classId,
//          @RequestParam Long userId
//  ){
//    return ResponseEntity.ok(assignmentService.getAssignmentsForStudent(classId, userId));
//  }
}
