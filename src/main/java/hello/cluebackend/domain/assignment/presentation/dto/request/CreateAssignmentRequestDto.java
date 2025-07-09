package hello.cluebackend.domain.assignment.presentation.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateAssignmentRequestDto {

  private String title;

  private String content;

  private String startDate;

  private String endDate;

  @Builder
  public CreateAssignmentRequestDto(){
    this.title = title;
    this.content = content;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}
