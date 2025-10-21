package hello.cluebackend.domain.student.submission.controller.dto.response;

import hello.cluebackend.domain.student.submission.domain.Submission;

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
