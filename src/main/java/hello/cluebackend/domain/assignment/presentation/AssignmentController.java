package hello.cluebackend.domain.assignment.presentation;

import hello.cluebackend.domain.assignment.exception.AssignmentNotFoundException;
import hello.cluebackend.domain.assignment.exception.UnauthorizedException;
import hello.cluebackend.domain.assignment.presentation.dto.request.AssignmentCreateRequestDto;
import hello.cluebackend.domain.assignment.presentation.dto.response.GetAssignmentResponseDto;
import hello.cluebackend.domain.assignment.service.AssignmentService;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assignments")
public class AssignmentController {
  private final AssignmentService assignmentService;
  private final JWTUtil jWTUtil;

  private Long jwtTokenTaker(HttpServletRequest request){
    String token = jWTUtil.getToken(request);
    Long userId = jWTUtil.getUserId(token);
    return userId;
  }

  @GetMapping("/{classId}")
  public ResponseEntity<List<GetAssignmentResponseDto>> getAllAssignment(
          HttpServletRequest request,
          @PathVariable Long classId
  ){
    Long userId = jwtTokenTaker(request);
    return ResponseEntity.ok(assignmentService.getAllAssignment(classId, userId));
  }

  @PostMapping("/{classId}")
  public ResponseEntity<?> createAssignment(
          HttpServletRequest request,
          @PathVariable Long classId,
          @ModelAttribute AssignmentCreateRequestDto requestDto
  ){
    Long userId = jwtTokenTaker(request);
    assignmentService.createAssignment(userId, classId, requestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("과제 생성 완료");
  }



//  @PostMapping(value = "/{classId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//  public ResponseEntity<?> createAssignment(
//          HttpServletRequest request,
//          @PathVariable Long classId,
//          @RequestPart("data") AssignmentCreateRequestDto requestDto,
//          @RequestPart(value = "file", required = false) MultipartFile file
//  ) {
//    String token = jWTUtil.getToken(request);
//    Long userId = jWTUtil.getUserId(token);
//
//    assignmentService.createAssignment(userId, classId, requestDto, file);
//    return ResponseEntity.status(HttpStatus.CREATED).build();
//  }
//
//  @DeleteMapping("/{classId}")
//  public ResponseEntity<Void> deleteAssignment(
//          HttpServletRequest request,
//          @PathVariable Long classId,
//          @RequestParam("assignmentId") Long assignmentId
//  ) {
//    String token = jWTUtil.getToken(request);
//    Long userId = jWTUtil.getUserId(token);
//
//    try {
//      assignmentService.deleteAssignment(classId, assignmentId, userId);
//      return ResponseEntity.noContent().build();
//    } catch (AssignmentNotFoundException e) {
//      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//    } catch (UnauthorizedException e) {
//      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//    }
//  }
//
//  @GetMapping("/{classId}")
//  public ResponseEntity<?> getAssignmentList(
//          HttpServletRequest request,
//          @PathVariable Long classId
//  ) {
//    String token = jWTUtil.getToken(request);
//    Long userId = jWTUtil.getUserId(token);
//
//    return ResponseEntity.ok(assignmentService.getAssignmentList(classId));
//  }

//  @GetMapping("/{classId}/check")
//  public ResponseEntity<?> getSubmitStatus(
//          HttpServletRequest request,
//          @PathVariable Long classId,
//          @RequestParam Long assignmentId
//  ) {
//    String token = jWTUtil.getToken(request);
//    Long userId = jWTUtil.getUserId(token);
//
//    return ResponseEntity.ok(assignmentService.getSubmissionStatus(classId, assignmentId));
//  }

//  @GetMapping("{classId}")
//  public ResponseEntity<List<AssignmentCardDto>> getAllAssignments(
//          @PathVariable Long classId,
//          @RequestParam Long userId
//  ){
//    return ResponseEntity.ok(assignmentService.getAssignmentsForStudent(classId, userId));
//  }
}
