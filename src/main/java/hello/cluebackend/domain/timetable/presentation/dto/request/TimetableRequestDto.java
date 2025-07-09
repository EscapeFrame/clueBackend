package hello.cluebackend.domain.timetable.presentation.dto.request;

import hello.cluebackend.domain.timetable.service.TimetableService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.processing.Pattern;

@Getter
public class TimetableRequestDto {
  @NotBlank(message = "학년은 필수입니다.")
  @Min(value = 1)
  @Max(value = 3)
  private String grade;

  @NotBlank(message = "반은 필수입니다.")
  @Min(value = 1)
  @Max(value = 4)
  private String classNumber;

  public TimetableRequestDto(String grade, String classNumber) {
    this.grade = grade;
    this.classNumber = classNumber;
  }
}
