package hello.cluebackend.domain.timetable.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TimetableRequestDto {
  private String grade;
  private String classNumber;
}
