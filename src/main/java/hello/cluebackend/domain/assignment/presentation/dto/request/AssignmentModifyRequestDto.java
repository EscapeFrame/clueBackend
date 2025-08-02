package hello.cluebackend.domain.assignment.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class AssignmentModifyRequestDto {
  private String title;
  private String Content;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
