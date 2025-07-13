package hello.cluebackend.domain.assignment.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AssignmentCardDto {
  private Long assignmentId;
  private String title;
  private String description;
  private LocalDateTime dueDate;

  private Boolean isSubmitted;
  private LocalDateTime submittedAt;

  private List<AssignmentContentDto> contents;
}
