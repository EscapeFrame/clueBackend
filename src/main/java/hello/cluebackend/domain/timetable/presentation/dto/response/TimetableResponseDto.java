package hello.cluebackend.domain.timetable.presentation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
public class TimetableResponseDto {

  private String period;
  private String subject;
  private String room;
  private String teacher;
  private String date;
  private String dayOfWeek;

  public static TimetableResponseDto fromMap(Map<String, Object> map) {
    TimetableResponseDto dto = new TimetableResponseDto();

    dto.period = (String) map.getOrDefault("PERIO", "");
    dto.subject = (String) map.getOrDefault("ITRT_CNTNT", "");
    dto.room = (String) map.getOrDefault("CLRM_NM", "");
    dto.teacher = (String) map.getOrDefault("ITRT_TCHR_NM", "");
    dto.date = (String) map.getOrDefault("ALL_TI_YMD", "");

    if (!dto.date.isEmpty() && dto.date.length() == 8) {
      try {
        LocalDate localDate = LocalDate.parse(dto.date, DateTimeFormatter.ofPattern("yyyyMMdd"));
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        dto.dayOfWeek = getKoreanDayName(dayOfWeek);
      } catch (Exception e) {
        dto.dayOfWeek = "";
      }
    }
    return dto;
  }

  private static String getKoreanDayName(DayOfWeek dayOfWeek) {
    return switch (dayOfWeek) {
      case MONDAY -> "월";
      case TUESDAY -> "화";
      case WEDNESDAY -> "수";
      case THURSDAY -> "목";
      case FRIDAY -> "금";
      case SATURDAY -> "토";
      case SUNDAY -> "일";
    };
  }
}