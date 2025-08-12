package hello.cluebackend.assignment.participation.application;

import hello.cluebackend.assignment.management.api.dto.response.GetSubmissionFile;
import hello.cluebackend.assignment.participation.domain.Submission;
import hello.cluebackend.assignment.participation.persistence.SubmissionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmissionCommandService {
  private final SubmissionRepository submissionRepository;

  public Submission findById(Long submissionId) {
    return submissionRepository.findById(submissionId)
            .orElseThrow(() -> new EntityNotFoundException("Submission not found : " + submissionId));
  }
}
