package hello.cluebackend.domain.submission.presentation.dto.response;

import hello.cluebackend.domain.submission.domain.Submission;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SubmissionResponse(
        String title,
        String content,
        LocalDateTime startDate,
        LocalDateTime endDate,

        String userName,

        UUID submissionId,
        boolean IsSubmitted,
        LocalDateTime submittedAt,

        List<SubmissionAttachmentResponse> submissionAttachmentResponses
) {
  public static SubmissionResponse from(Submission submission, List<SubmissionAttachmentResponse> sa) {
    return new SubmissionResponse(
            submission.getAssignment().getTitle(),
            submission.getAssignment().getContent(),
            submission.getAssignment().getStartDate(),
            submission.getAssignment().getEndDate(),
            submission.getUser().getUsername(),
            submission.getSubmissionId(),
            submission.getIsSubmitted(),
            submission.getSubmittedAt(),
            sa
    );
  }
}
