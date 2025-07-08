package hello.cluebackend.domain.timetable.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
public class TimetableResponseDto {

  private String period; // 교시
  private String subject; // 과목 이름
  private String date; // 날짜
  private String dayOfWeek; // 요일

  public static TimetableResponseDto fromMap(Map<String, Object> map) {
    TimetableResponseDto dto = new TimetableResponseDto();

    dto.period = (String) map.getOrDefault("PERIO", "");
    dto.subject = (String) map.getOrDefault("ITRT_CNTNT", "");
    dto.date = (String) map.getOrDefault("ALL_TI_YMD", "");

    if (!dto.date.isEmpty() && dto.date.length() == 8) {
      try {
        LocalDate localDate = LocalDate.parse(dto.date, DateTimeFormatter.ofPattern("yyyyMMdd"));
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        dto.dayOfWeek = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN);
      } catch (Exception e) {
        dto.dayOfWeek = "";
      }
    }else{
      dto.dayOfWeek="error";
    }
    return dto;
  }
}