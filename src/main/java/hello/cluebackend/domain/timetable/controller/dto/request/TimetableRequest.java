package hello.cluebackend.domain.timetable.controller.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TimetableRequest {
  @NotBlank(message = "학년은 필수입니다.")
  @Min(value = 1) @Max(value = 3)
  private String grade;

  @NotBlank(message = "반은 필수입니다.")
  @Min(value = 1) @Max(value = 4)
  private String classNumber;

  public TimetableRequest(String grade, String classNumber) {
    this.grade = grade;
    this.classNumber = classNumber;
  }
}
