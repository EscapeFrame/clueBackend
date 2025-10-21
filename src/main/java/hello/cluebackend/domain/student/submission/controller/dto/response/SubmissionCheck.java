package hello.cluebackend.domain.student.submission.controller.dto.response;

import hello.cluebackend.domain.student.submission.domain.Submission;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubmissionCheck (
        String userName,
        int classNumberGrade,
        UUID submissionId,
        boolean isSubmitted,
        LocalDateTime submittedAt
){
  public static SubmissionCheck from(Submission submission) {
    return new SubmissionCheck(
            submission.getUser().getUsername(),
            submission.getUser().getClassCode(),
            submission.getSubmissionId(),
            submission.getIsSubmitted(),
            submission.getSubmittedAt()
    );
  }
}