package hello.cluebackend.domain.assignment.presentation;


import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.exception.AssignmentNotFoundException;
import hello.cluebackend.domain.assignment.exception.UnauthorizedException;
import hello.cluebackend.domain.assignment.presentation.dto.request.AssignmentCreateRequestDto;
import hello.cluebackend.domain.assignment.service.AssignmentService;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assignments")
public class AssignmentController {
  private final AssignmentService assignmentService;
  private final JWTUtil jWTUtil;

  @PostMapping(value = "/{classId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createAssignment(
          HttpServletRequest request,
          @PathVariable Long classId,
          @RequestPart("data") AssignmentCreateRequestDto requestDto,
          @RequestPart(value = "file", required = false) MultipartFile file
  ) {
    String token = jWTUtil.getToken(request);
    Long userId = jWTUtil.getUserId(token);

    assignmentService.createAssignment(userId, classId, requestDto, file);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/{classId}")
  public ResponseEntity<Void> deleteAssignment(
          HttpServletRequest request,
          @PathVariable Long classId,
          @RequestParam("assignmentId") Long assignmentId
  ) {
    String token = jWTUtil.getToken(request);
    Long userId = jWTUtil.getUserId(token);

    try {
      assignmentService.deleteAssignment(classId, assignmentId, userId);
      return ResponseEntity.noContent().build();
    } catch (AssignmentNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (UnauthorizedException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
  }

  @GetMapping("/{classId}")
  public ResponseEntity<?> getAssignmentList(
          HttpServletRequest request,
          @PathVariable Long classId
  ) {
    String token = jWTUtil.getToken(request);
    Long userId = jWTUtil.getUserId(token);

    return ResponseEntity.ok(assignmentService.getAssignmentList(classId));
  }

  @GetMapping("/{classId}/check")
  public ResponseEntity<?> getSubmitStatus(
          HttpServletRequest request,
          @PathVariable Long classId,
          @RequestParam Long assignmentId
  ) {
    String token = jWTUtil.getToken(request);
    Long userId = jWTUtil.getUserId(token);

    return ResponseEntity.ok(assignmentService.getSubmissionStatus(classId, assignmentId));
  }

//  @GetMapping("{classId}")
//  public ResponseEntity<List<AssignmentCardDto>> getAllAssignments(
//          @PathVariable Long classId,
//          @RequestParam Long userId
//  ){
//    return ResponseEntity.ok(assignmentService.getAssignmentsForStudent(classId, userId));
//  }
}
