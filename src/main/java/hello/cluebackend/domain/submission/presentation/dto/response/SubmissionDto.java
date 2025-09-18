package hello.cluebackend.domain.submission.presentation.dto.response;

import hello.cluebackend.domain.submission.domain.Submission;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubmissionDto(
        UUID submissionId,
        boolean IsSubmitted,
        LocalDateTime submittedAt
) {
  public static SubmissionDto from(Submission submission) {
    return new SubmissionDto(
            submission.getSubmissionId(),
            submission.getIsSubmitted(),
            submission.getSubmittedAt()
    );
  }
}
