package hello.cluebackend.domain.assignment.presentation.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class AssignmentCreateRequestDto {
  private String title;
  private String content;
  private LocalDateTime startData;
  private LocalDateTime endDate;
}
