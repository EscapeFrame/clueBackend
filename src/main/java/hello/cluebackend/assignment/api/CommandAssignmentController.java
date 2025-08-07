package hello.cluebackend.assignment.api;

import hello.cluebackend.assignment.api.dto.response.GetAllAssignment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assignment")
@RequiredArgsConstructor
@Slf4j
public class CommandAssignmentController {
  // TODO : 학생 미제출 과제 전체 조회


  // TODO : 학생 수업실 과제 페이지 과제 젠체 조회
  @GetMapping("/get/{classId}")
  public ResponseEntity<List<GetAllAssignment>> getClassroomAssignment(
          HttpServletRequest request,
          @PathVariable Long classId
  ) {
    try {
      String token = jwtUtil.getToken(request);
      Long userId = jwtUtil.getUserId(token);

      List<GetAllAssignment> assignments = assignmentService.findAllAssignment(userId, classId);
//      List<ResponseGetAllAssignmentAttachment> attachments = assignmentA

      return ResponseEntity.ok(assignments);
    } catch (Exception e){
      log.error("Error while getting assignments", e);
      return ResponseEntity.internalServerError().build();
    }
  }
}
