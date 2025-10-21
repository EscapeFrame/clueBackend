package hello.cluebackend.domain.student.subject.controller;

import hello.cluebackend.domain.student.subject.service.SubjectService;
import hello.cluebackend.domain.student.subject.controller.dto.request.SubjectRequest;
import hello.cluebackend.domain.student.subject.controller.dto.response.SubjectResponse;
import hello.cluebackend.global.common.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subject")
public class SubjectController {
  private final SubjectService subjectService;

  @GetMapping("/{subjectId}")
  public ResponseEntity<SubjectResponse> findById(
          @PathVariable Long subjectId,
          @CurrentUser UUID userId
  ) {
    SubjectResponse response = subjectService.findSubject(subjectId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/")
  public ResponseEntity<List<SubjectResponse>> findAllByGrade(
          @RequestParam("grade") int grade,
          @CurrentUser UUID userId
  ) {
    List<SubjectResponse> responses = subjectService.findAllSubject(grade);
    return ResponseEntity.ok(responses);
  }

  @PostMapping("/")
  public ResponseEntity<SubjectResponse> create(@CurrentUser UUID userId, @RequestBody SubjectRequest request) {
    SubjectResponse response = subjectService.createSubject(request, userId);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{subjectId}")
  public ResponseEntity<?> delete(
          @PathVariable Long subjectId,
          @CurrentUser UUID userId
  ) {
    subjectService.deleteSubject(subjectId, userId);
    return ResponseEntity.noContent().build();
  }


  @PatchMapping("/{subjectId}")
  public ResponseEntity<SubjectResponse> update(
          @PathVariable Long subjectId,
          @CurrentUser UUID userId,
          @RequestBody SubjectRequest subjectRequest
  ) {
    SubjectResponse response = subjectService.updateSubject(subjectId, subjectRequest);
    return  ResponseEntity.ok(response);
  }
}
