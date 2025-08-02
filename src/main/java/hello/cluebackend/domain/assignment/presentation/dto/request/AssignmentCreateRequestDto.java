package hello.cluebackend.domain.assignment.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class AssignmentCreateRequestDto {
  @NotBlank(message = "과제 제목은 필수입니다.")
  private String title;


  private String content;

  @NotBlank(message = "시작 일자는 필수입니다.")
  private LocalDateTime startData;

  @NotBlank(message = "마감일은 필수입니다.")
  private LocalDateTime endDate;
}