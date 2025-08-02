package hello.cluebackend.domain.assignment.presentation;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.presentation.dto.request.AssignmentCreateRequestDto;
import hello.cluebackend.domain.assignment.service.AssignmentService;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assignments")
public class AssignmentController {
  private final AssignmentService assignmentService;
  private final JWTUtil jWTUtil;

  // 선생님 과제 생성하기
  @PostMapping("/create/{classId}")
  public ResponseEntity<Assignment> createAssignment(
          HttpServletRequest request,
          @PathVariable Long classId,
          @Valid @RequestBody AssignmentCreateRequestDto requestDto
  ){
    try{
      String token = jWTUtil.getToken(request);
      Long userId = jWTUtil.getUserId(token);
      Assignment createAssignment = assignmentService.createAssignment(userId, classId, requestDto);
      return new ResponseEntity<>(createAssignment, HttpStatus.CREATED);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

//  // 미제출 과제 전체 조회(학생)
//  @GetMapping("/")
//  public ResponseEntity<List<StudentAssignmentRemain>> getStudentRemainCard (HttpServletRequest request){
//    String token = jWTUtil.getToken(request);
//    Long userId = jWTUtil.getUserId(token);
//
//    return ResponseEntity.ok(assignmentService.getUnsubmittedAssignments(userId));
//  }
//
//  // 교실 전체 과제 조회하기
//  @GetMapping("/{classId}")
//  public ResponseEntity<List<GetAssignmentResponseDto>> getAllAssignment(HttpServletRequest request, @PathVariable Long classId){
//    String token = jWTUtil.getToken(request);
//    Long userId = jWTUtil.getUserId(token);
//
//    return ResponseEntity.ok(assignmentService.getAllAssignment(classId, userId));
//  }
//
//  // 특정 교실의 학생들의 과제 제출 여부
//  @GetMapping("/{assignmentId}/submissions")
//  public ResponseEntity<List<AssignmentSubmissionStatusDto>> getAssignmentSubmissionStatus(HttpServletRequest request, @PathVariable Long assignmentId){
//    String token = jWTUtil.getToken(request);
//    Long userId = jWTUtil.getUserId(token);
//
//    return ResponseEntity.ok(assignmentService.checkAssignments(assignmentId));
//  }
//
//  // 특정 수업에 학생들의 단일 과제 제출 여부 확인 API
//  @GetMapping("/assignments/classroom/{assignmentId}/{userId}")
//  public ResponseEntity<AssignmentSubmissionDto> getAssignmentSubmissionPerson(HttpServletRequest request, @PathVariable Long assignmentId, @PathVariable Long userId) {
//    String token = jWTUtil.getToken(request);
//    Long userId = jWTUtil.getUserId(token);
//
//    return ResponseEntity.ok(assignmentService.checkAssignment(assignmentId,userId));
//  }

//  //과제 내용 수정
//  @PatchMapping("/api/assignments/{assignmentId}")
//  public ResponseEntity modifyAssignment(HttpServletRequest request, @PathVariable Long assignmentId, @RequestBody AssignmentModifyRequestDto assignmentModifyRequestDto){
//    jwtTokenTaker(request);
//
//    return ResponseEntity.ok(assignmentService.modifyAssignment(assignmentId,assignmentModifyRequestDto));
//  }

//  // 과제 삭제
//  @DeleteMapping("/api/assignments/{assignmentId}")
//  public ResponseEntity deleteAssignment(HttpServlet request, @PathVariable Long assignmentId){
//    jwtTokenTaker(request);
//    return ResponseEntity.ok(assignmentService.deleteAssignment(assignmentId));
//  }
}
