package hello.cluebackend.domain.assignment.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
public class StudentGetAssignment {
  private Long assignmentId;
  private String title;
  private String description;
  private LocalDateTime dueDate;

  private Boolean isSubmitted;
  private LocalDateTime submittedAt;

  private List<AssignmentContentDto> contents;
}
