package hello.cluebackend.domain.assignment.presentation;

import hello.cluebackend.domain.assignment.presentation.dto.request.CreateAssignmentRequestDto;
import hello.cluebackend.domain.assignment.presentation.dto.request.GetAssignmentRequestDto;
import hello.cluebackend.domain.assignment.presentation.dto.response.AllStudentAssignmentResponseDto;
import hello.cluebackend.domain.assignment.presentation.dto.response.AssignmentStatus;
import hello.cluebackend.domain.assignment.presentation.dto.response.CreateAssignmentResponseDto;
import hello.cluebackend.domain.assignment.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assignment")
public class AssignmentController {
  private final AssignmentService assignmentService;

  // TODO : 해당 교실의 모든 과제 보기
  @GetMapping("/{classId}")
  public ResponseEntity<List<AllStudentAssignmentResponseDto>> getAllAssignment(
          @PathVariable int classId
  ){
    List<AllStudentAssignmentResponseDto> assignments = assignmentService.allStudentAssignment(classId);
    return ResponseEntity.ok(assignments);
  }
//
//  // 해당 교실을 만든 사람 혹은 권한이 있는 사람만 수정 및 삭제가 가능
//  // TODO : 과제 생성 ( 선생님 )
//  @PostMapping("/{classId}")
//  public ResponseEntity<CreateAssignmentResponseDto> createAssignment(
//          @PathVariable Long classId,
//          @Valid @RequestBody CreateAssignmentRequestDto request,
//          Authentication authentication
//  ){
//    CreateAssignmentResponseDto response = assignmentService.create(classId, request, authentication);
//    return ResponseEntity.status(HttpStatus.CREATED).body(response);
//  }
//
//  // TODO : 과제 삭제 ( 선생님 )
//  @DeleteMapping("/{assignmentId}")
//  public ResponseEntity<String> deleteAssignment(
//          @PathVariable Long assignmentId,
//          Authentication authentication
//  ){
//    assignmentService.delete(assignmentId, authentication);
//    return ResponseEntity.ok("과제 삭제 성공");
//  }
//
//  // TODO :  과제 수정 ( 선생님 )
//  @PatchMapping("/{classId}/{assignmentId}")
//  public ResponseEntity<String> updateAssignment(
//          @PathVariable int classId,
//          @PathVariable int assignmentId,
//          Authentication authentication
//  ){
//    return ResponseEntity.ok("수정 성공");
//  }
//
//  // TODO : 학생 특정 수업 과제 조회 ( 선생님, 학생 )
//  @GetMapping("/{assignmentId}")
//  public Optional<GetAssignmentRequestDto> specalizeGetAssignment(
//          @PathVariable Long assignmentId,
//          Authentication authentication
//  ){
//    return assignmentService.spelizationGetAssignment(assignmentId, authentication);
//  }
//
//  // TODO : 학생 개인 전체 과제 조회 ( 학생 )
//  @GetMapping("/assignemtnStatus")
//  public List<AssignmentStatus> getAssignmentStatuses(
//          Authentication authentication
//  ){
//    return assignmentService.getAssignmentStatus(authentication);
//  }
//
//  // TODO : 학생 제출한 과제 보기
//
//  // TODO : 과제 제출
//
//  // TODO : 과제 제출 취소
//
//  // TODO : 과제 다시 제출



}
