package hello.cluebackend.domain.timetable.service;

import hello.cluebackend.domain.timetable.presentation.dto.request.TimetableRequestDto;
import hello.cluebackend.domain.timetable.presentation.dto.response.TimetableResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimetableService {

  // Constants
  private static final String TIMETABLE_PATH = "/hisTimetable";
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
  private static final DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy");
  private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MM");
  private static final ZoneId KOREA_TIMEZONE = ZoneId.of("Asia/Seoul");
  private static final int FIRST_SEMESTER_END_MONTH = 8;
  private static final String FIRST_SEMESTER = "1";
  private static final String SECOND_SEMESTER = "2";
  private static final int API_TIMEOUT_SECONDS = 10;
  private static final int RETRY_ATTEMPTS = 2;
  private static final Duration RETRY_DELAY = Duration.ofSeconds(1);

  @Value("${spring.neis.api.host}")
  private String neisApiHost;

  @Value("${spring.neis.api.key}")
  private String neisApiKey;

  @Value("${spring.neis.api.atpt-code:C10}")
  private String atptCode;

  @Value("${spring.neis.api.school-code:7150658}")
  private String schoolCode;

  private final WebClient webClient;

  public Mono<List<TimetableResponseDto>> getTodayTimetable(TimetableRequestDto request) {
    LocalDate today = LocalDate.now(KOREA_TIMEZONE);
    String todayStr = today.format(DATE_FORMATTER);

    log.info("오늘 시간표 조회 요청 - Grade: {}, Class: {}, Date: {}",
            request.getGrade(), request.getClassNumber(), todayStr);

    return fetchTimetable(request, todayStr, todayStr);
  }

  public Mono<List<TimetableResponseDto>> getWeeklyTimetable(TimetableRequestDto request) {
    LocalDate now = LocalDate.now(KOREA_TIMEZONE);
    String from = now.with(java.time.DayOfWeek.MONDAY).format(DATE_FORMATTER);
    String to = now.with(java.time.DayOfWeek.FRIDAY).format(DATE_FORMATTER);

    log.info("주간 시간표 조회 요청 - Grade: {}, Class: {}, Period: {} ~ {}",
            request.getGrade(), request.getClassNumber(), from, to);

    return fetchTimetable(request, from, to);
  }

  private Mono<List<TimetableResponseDto>> fetchTimetable(TimetableRequestDto request, String from, String to) {
    LocalDate now = LocalDate.now(KOREA_TIMEZONE);
    String year = now.format(YEAR_FORMATTER);
    String semester = determineSemester(now);

    log.debug("NEIS API 호출 파라미터 - Year: {}, Semester: {}, From: {}, To: {}",
            year, semester, from, to);

    return webClient.get()
            .uri("https://" + neisApiHost + TIMETABLE_PATH +
                    "?KEY=" + neisApiKey +
                    "&Type=json" +
                    "&pIndex=1" +
                    "&pSize=100" +
                    "&ATPT_OFCDC_SC_CODE=" + atptCode +
                    "&SD_SCHUL_CODE=" + schoolCode +
                    "&AY=" + year +
                    "&SEM=" + semester +
                    "&GRADE=" + request.getGrade() +
                    "&CLASS_NM=" + request.getClassNumber() +
                    "&TI_FROM_YMD=" + from +
                    "&TI_TO_YMD=" + to)
//            .uri(uriBuilder -> uriBuilder
//                    .scheme("https")
//                    .host(neisApiHost)
//                    .path(TIMETABLE_PATH)
//                    .queryParam("KEY", neisApiKey)
//                    .queryParam("Type", "json")
//                    .queryParam("pIndex", "1")
//                    .queryParam("pSize", "100")
//                    .queryParam("ATPT_OFCDC_SC_CODE", atptCode)
//                    .queryParam("SD_SCHUL_CODE", schoolCode)
//                    .queryParam("AY", year)
//                    .queryParam("SEM", semester)
//                    .queryParam("GRADE", request.getGrade())
//                    .queryParam("CLASS_NM", request.getClassNumber())
//                    .queryParam("TI_FROM_YMD", from)
//                    .queryParam("TI_TO_YMD", to)
//                    .build())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError,
                    response -> response.bodyToMono(String.class)
                            .flatMap(body -> {
                              log.error("NEIS API 클라이언트 오류 - Status: {}, Body: {}",
                                      response.statusCode(), body);
                              return Mono.error(new IllegalArgumentException("잘못된 요청: " + body));
                            }))
            .onStatus(HttpStatusCode::is5xxServerError,
                    response -> {
                      log.error("NEIS API 서버 오류 - Status: {}", response.statusCode());
                      return Mono.error(new RuntimeException("NEIS 서버 오류 발생"));
                    })
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .timeout(Duration.ofSeconds(API_TIMEOUT_SECONDS))
            .retryWhen(Retry.backoff(RETRY_ATTEMPTS, RETRY_DELAY)
                    .filter(throwable -> !(throwable instanceof WebClientResponseException.BadRequest))
                    .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                      log.error("NEIS API 재시도 횟수 초과");
                      return new RuntimeException("NEIS API 호출 실패 - 호출 시간 초과");
                    }))
            .map(this::parseTimetableResponse)
            .doOnSuccess(result -> log.info("시간표 조회 성공 - 결과 개수: {}", result.size()))
            .doOnError(error -> log.error("시간표 조회 실패", error));
  }

  private String determineSemester(LocalDate date) {
    int month = Integer.parseInt(date.format(MONTH_FORMATTER));
    return (month <= FIRST_SEMESTER_END_MONTH) ? FIRST_SEMESTER : SECOND_SEMESTER;
  }

  private List<TimetableResponseDto> parseTimetableResponse(Map<String, Object> response) {
    try {
      return Optional.ofNullable(response.get("hisTimetable"))
              .filter(List.class::isInstance)
              .map(obj -> (List<?>) obj)
              .filter(list -> list.size() >= 2)
              .map(list -> list.get(1))
              .filter(Map.class::isInstance)
              .map(obj -> (Map<?, ?>) obj)
              .map(map -> map.get("row"))
              .filter(List.class::isInstance)
              .map(obj -> (List<?>) obj)
              .map(this::convertToTimetableResponseList)
              .orElse(List.of());
    } catch (Exception e) {
      log.error("시간표 파싱 오류", e);
      return List.of();
    }
  }

  @SuppressWarnings("unchecked")
  private List<TimetableResponseDto> convertToTimetableResponseList(List<?> rowList) {
    return rowList.stream()
            .filter(Map.class::isInstance)
            .map(item -> (Map<String, Object>) item)
            .map(TimetableResponseDto::fromMap)
            .collect(Collectors.toList());
  }
}