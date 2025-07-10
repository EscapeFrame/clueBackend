package hello.cluebackend.domain.assignment.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AllStudentAssignmentResponseDto {
  private String title;
  private String content;
  private String startDate;
  private String endDate;
}
