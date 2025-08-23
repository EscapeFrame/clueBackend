package hello.cluebackend.domain.assignment.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GetAllSubmissionCheckDto {
  private Long submissionId;
  private Long userId;
  private String userName;
  private int userClassCode;
  private boolean isSubmitted;
  private LocalDateTime submittedAt;
  private LocalDateTime assignmentEndDate;
}
