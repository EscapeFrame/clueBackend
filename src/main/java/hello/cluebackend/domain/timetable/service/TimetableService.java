package hello.cluebackend.domain.timetable.service;

import hello.cluebackend.domain.timetable.presentation.dto.request.TimetableRequestDto;
import hello.cluebackend.domain.timetable.presentation.dto.response.TimetableResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimetableService {

  @Value("${spring.neis.api.host}") // https:// 제거한 host만 사용하도록
  private String neisApiHost;

  @Value("${spring.neis.api.key}")
  private String neisApiKey;

  @Value("${spring.neis.api.atpt-code:C10}")
  private String atptCode;

  @Value("${spring.neis.api.school-code:7150658}")
  private String schoolCode;

  private final WebClient webClient;

  private static final String TIMETABLE_PATH = "/hisTimetable";

  public Mono<List<TimetableResponseDto>> getTodayTimetable(TimetableRequestDto request) {
    String today = LocalDate.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    return fetchTimetable(request, today, today);
  }

  public Mono<List<TimetableResponseDto>> getWeeklyTimetable(TimetableRequestDto request) {
    LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
    String from = now.with(java.time.DayOfWeek.MONDAY).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    String to = now.with(java.time.DayOfWeek.FRIDAY).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    return fetchTimetable(request, from, to);
  }

  private Mono<List<TimetableResponseDto>> fetchTimetable(TimetableRequestDto request, String from, String to) {
    return webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .scheme("https")
                    .host(neisApiHost)
                    .path(TIMETABLE_PATH)
                    .queryParam("KEY", neisApiKey)
                    .queryParam("Type", "json")
                    .queryParam("pIndex", "1")
                    .queryParam("pSize", "100")
                    .queryParam("ATPT_OFCDC_SC_CODE", atptCode)
                    .queryParam("SD_SCHUL_CODE", schoolCode)
                    .queryParam("GRADE", request.getGrade())
                    .queryParam("CLASS_NM", request.getClassNumber())
                    .queryParam("TI_FROM_YMD", from)
                    .queryParam("TI_TO_YMD", to)
                    .build())
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), response -> response.bodyToMono(String.class).flatMap(body -> Mono.error(new IllegalArgumentException("잘못된 요청: " + body))))
            .onStatus(status -> status.is5xxServerError(), response -> Mono.error(new RuntimeException("NEIS 서버 오류 발생")))
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .timeout(Duration.ofSeconds(10))
            .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)).filter(throwable -> !(throwable instanceof WebClientResponseException.BadRequest)))
            .map(this::parseTimetableResponse);
  }

  private List<TimetableResponseDto> parseTimetableResponse(Map<String, Object> response) {
    try {
      Object timetableObj = response.get("hisTimetable");
      if (timetableObj instanceof List<?> timetableList && timetableList.size() >= 2) {
        Object secondElement = timetableList.get(1);
        if (secondElement instanceof Map<?, ?> secondMap) {
          Object rowObj = secondMap.get("row");
          if (rowObj instanceof List<?> rowList) {
            return rowList.stream()
                    .filter(Map.class::isInstance)
                    .map(item -> (Map<String, Object>) item)
                    .map(TimetableResponseDto::fromMap)
                    .collect(Collectors.toList());
          }
        }
      }
    } catch (Exception e) {
      log.error("시간표 파싱 오류", e);
    }
    return List.of();
  }
}