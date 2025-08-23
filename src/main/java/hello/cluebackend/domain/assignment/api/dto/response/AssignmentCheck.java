package hello.cluebackend.domain.assignment.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AssignmentCheck {
  private String userName;
  private int classNumberGrade;
  private boolean isSubmitted;
  private LocalDateTime submittedAt;
}