package hello.cluebackend.assignment.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AssignmentCreateRequestDto {
  @NotBlank(message = "과제 제목은 필수입니다.")
  private String title;

  private String content;

  @NotNull(message = "시작 일자는 필수입니다.")
  private LocalDateTime startDate;

  @NotNull(message = "마감일은 필수입니다.")
  private LocalDateTime endDate;

  @Builder
  public AssignmentCreateRequestDto(String title, String content, LocalDateTime startDate, LocalDateTime endDate){
    this.title = title;
    this.content = content;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}