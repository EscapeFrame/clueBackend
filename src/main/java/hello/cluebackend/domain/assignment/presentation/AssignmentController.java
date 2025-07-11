package hello.cluebackend.domain.assignment.presentation;


import hello.cluebackend.domain.assignment.presentation.dto.response.GetAssignment;
import hello.cluebackend.domain.assignment.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assignment")
public class AssignmentController {
  private final AssignmentService assignmentService;

  @GetMapping("{classId}")
  public List<GetAssignment> getAllAssignments(
          @PathVariable Long classId,
          @RequestParam Long userId
  ){
    return assignmentService.getAllAssignments(classId, userId);
  }
}
