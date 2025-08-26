package hello.cluebackend.domain.assignment.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubmissionCheck {
  private String userName;
  private int classNumberGrade;
  private Long submissionId;
  private boolean isSubmitted;
  private LocalDateTime submittedAt;
}