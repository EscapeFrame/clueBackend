package hello.cluebackend.application.timetable.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

@Getter
@Builder
public class TimetableResponse {
  @JsonProperty("period") private final String period;   // 교시
  @JsonProperty("subject") private final String subject;  // 과목 이름
  @JsonProperty("date") private final String date;     // 날짜 (yyyyMMdd)
  @JsonProperty("dayOfWeek") private final String dayOfWeek;// 요일

  public static TimetableResponse fromMap(Map<String, Object> map) {
    String period = (String) map.getOrDefault("PERIO", "");
    String subject = (String) map.getOrDefault("ITRT_CNTNT", "");
    String date = (String) map.getOrDefault("ALL_TI_YMD", "");

    String dayOfWeek = parseDayOfWeek(date);

    return TimetableResponse.builder()
            .period(period)
            .subject(subject)
            .date(date)
            .dayOfWeek(dayOfWeek)
            .build();
  }

  private static String parseDayOfWeek(String date) {
    if (date != null && date.length() == 8) {
      try {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN); // "월"
      } catch (Exception e) {
        return null;
      }
    }
    return null;
  }

  @JsonCreator
  public TimetableResponse(
          @JsonProperty("period") String period,
          @JsonProperty("subject") String subject,
          @JsonProperty("date") String date,
          @JsonProperty("dayOfWeek") String dayOfWeek
  ) {
    this.period = period;
    this.subject = subject;
    this.date = date;
    this.dayOfWeek = dayOfWeek;
  }
}