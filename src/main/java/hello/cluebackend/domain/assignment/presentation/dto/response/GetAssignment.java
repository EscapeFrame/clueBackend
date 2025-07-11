package hello.cluebackend.domain.assignment.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class GetAssignment {
  private Long assignmentId;
  private String title;
  private String content;
  private String startDate;
  private String endDate;
}
