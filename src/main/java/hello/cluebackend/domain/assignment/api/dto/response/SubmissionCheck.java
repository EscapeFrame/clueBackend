package hello.cluebackend.domain.assignment.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SubmissionCheck {
  private String userName;
  private int classNumberGrade;
  private UUID submissionId;
  private boolean isSubmitted;
  private LocalDateTime submittedAt;
}