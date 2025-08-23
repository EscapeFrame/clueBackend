package hello.cluebackend.domain.submission.api;

import hello.cluebackend.domain.submission.api.dto.response.SubmissionDto;
import hello.cluebackend.domain.submission.application.SubmissionCommandService;
import hello.cluebackend.domain.submission.domain.Submission;
import hello.cluebackend.global.common.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@Slf4j
public class SubmissionCommandController {
  private final SubmissionCommandService submissionCommandService;

  // 할당된 과제 전체 조회
  @GetMapping("/assignment/{assignmentId}")
  public ResponseEntity<List<SubmissionDto>> findAllSubmission(@CurrentUser Long id, @PathVariable Long assignmentId) {
    List<Submission> submissions = submissionCommandService.findAllByAssignmentId(assignmentId);

    List<SubmissionDto> result = submissions.stream()
            .map(s -> SubmissionDto.builder()
                    .title(s.getAssignment().getTitle())
                    .startDate(s.getAssignment().getStartDate())
                    .endDate(s.getAssignment().getEndDate())
                    .userName(s.getUser().getUsername())
                    .isSubmitted(s.getIsSubmitted())
                    .submittedAt(s.getSubmittedAt())
                    .build()
            )
            .toList();

    return ResponseEntity.ok(result);
  }

  // 할당된 과제 단일 조회
  @GetMapping("/{submissionId}")
  public ResponseEntity<SubmissionDto> findSubmission(@CurrentUser Long id, @PathVariable Long assignmentId) {

    Submission s = submissionCommandService.findByAssignmentId(assignmentId);

    SubmissionDto result = SubmissionDto.builder()
            .title(s.getAssignment().getTitle())
            .startDate(s.getAssignment().getStartDate())
            .endDate(s.getAssignment().getEndDate())
            .userName(s.getUser().getUsername())
            .isSubmitted(s.getIsSubmitted())
            .submittedAt(s.getSubmittedAt())
            .build();

    return ResponseEntity.ok(result);
  }

  // 할당된 과제의 파일 전체 조회


  // TODO : 메인 페이지 학생 미제출 과제 전체 조회, 메인


  // TODO : 수업실 과제 전체 조회 및 학생 첨부 파일 전체 조회, 학습실 - 과제


  // TODO : 수업실 과제 단일 세부 조회 및 학생,선생 첨부 파일 전체 조회 , 학습실 - 과제세부확인


}
