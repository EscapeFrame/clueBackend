package hello.cluebackend.assignment.participation.application;

import hello.cluebackend.assignment.management.api.dto.response.GetSubmissionFile;
import hello.cluebackend.assignment.participation.domain.Submission;
import hello.cluebackend.assignment.participation.persistence.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmissionCommandService {
  private final SubmissionRepository submissionRepository;

  public Submission findById(Long submissionId) {
    return submissionRepository.findById(submissionId).get();
  }

  public GetSubmissionFile getSubmissionGrade(Submission submission) {
    return submissionRepository.findByIdWithAttachment(submission);
  }
}
