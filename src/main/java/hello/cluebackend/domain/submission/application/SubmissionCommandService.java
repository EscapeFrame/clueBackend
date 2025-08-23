package hello.cluebackend.domain.submission.application;

import hello.cluebackend.domain.assignment.api.dto.response.AssignmentCheck;
import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.persistence.AssignmentRepository;
import hello.cluebackend.domain.submission.domain.Submission;
import hello.cluebackend.domain.submission.persistence.SubmissionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionCommandService {
  private final SubmissionRepository submissionRepository;
  private final AssignmentRepository assignmentRepository;

  // 과제 제출 여부 확인 API
  public List<AssignmentCheck> checkAssignment(Long assignmentId) {
    Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() -> new EntityNotFoundException("해당 과제를 찾을수 없습니다."));
    List<Submission> submissions =  submissionRepository.findAllByAssignment(assignment);
    return submissions.stream()
            .map(s -> AssignmentCheck.builder()
                    .userName(s.getUser().getUsername())
                    .classNumberGrade(s.getUser().getClassCode())
                    .isSubmitted(s.getIsSubmitted())
                    .submittedAt(s.getSubmittedAt())
                    .build()
            )
            .toList();
  }

  public List<Submission> findAllByAssignmentId(Long assignmentId) {
    Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 과제를 찾을수 없습니다."));

    List<Submission> result = submissionRepository.findAllByAssignment(assignment);
    return result;
  }

  public Submission findByAssignmentId(Long assignmentId) {
    Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 과제를 찾을수 없습니다."));

    Submission result = submissionRepository.findByAssignment(assignment);
    return result;
  }

//  public Submission findById(Long submissionId) {
//    return submissionRepository.findById(submissionId)
//            .orElseThrow(() -> new EntityNotFoundException("Submission not found : " + submissionId));
//  }
//
//  public GetSubmissionFile getSubmissionGrade(Submission submission) {
//
//  }
}
