package hello.cluebackend.domain.timetable.service;

import hello.cluebackend.application.timetable.dto.request.TimetableRequest;
import hello.cluebackend.application.timetable.dto.response.TimetableResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheTimetableService {
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
  private static final ZoneId KOREA_TIMEZONE = ZoneId.of("Asia/Seoul");

  private final TimetableService timetableService;
  private final RedisTemplate<String, Object> redisTemplate;

  public Mono<List<TimetableResponse>> getTodayTimetableWithCache(TimetableRequest request) {
    LocalDate today = LocalDate.now(KOREA_TIMEZONE);
    String todayStr = today.format(DATE_FORMATTER);

    String cacheKey = buildCacheKey(request, todayStr, todayStr);

    List<TimetableResponse> cached = (List<TimetableResponse>) redisTemplate.opsForValue().get(cacheKey);
    if (cached != null) {
      log.info("Redis 캐시에서 시간표 조회 성공 - key: {}", cacheKey);
      return Mono.just(cached);
    }

    log.warn("Redis 캐시에 없음. NEIS API 호출 시도 - key: {}", cacheKey);
    return timetableService.getTodayTimetable(request)
            .doOnSuccess(result -> {
              if (!result.isEmpty()) {
                redisTemplate.opsForValue().set(cacheKey, result, Duration.ofDays(1));
                log.info("NEIS API 응답 Redis 캐싱 완료 - key: {}", cacheKey);
              } else {
                log.warn("NEIS API 응답이 비어있음 - key: {}", cacheKey);
              }
            })
            .doOnError(error -> log.error("Fallback API 호출 실패 - key: {}", cacheKey, error));
  }

  public Mono<List<TimetableResponse>> getWeeklyTimetableWithCache(TimetableRequest request) {
    LocalDate today = LocalDate.now(KOREA_TIMEZONE);
    String from = today.with(java.time.DayOfWeek.MONDAY).format(DATE_FORMATTER);
    String to = today.with(java.time.DayOfWeek.FRIDAY).format(DATE_FORMATTER);

    String cacheKey = buildCacheKey(request, from, to);

    List<TimetableResponse> cached = (List<TimetableResponse>) redisTemplate.opsForValue().get(cacheKey);
    if (cached != null) {
      log.info("Redis 캐시에서 시간표 조회 성공 - key: {}", cacheKey);
      return Mono.just(cached);
    }

    log.warn("Redis 캐시에 없음. NEIS API 호출 시도 - key: {}", cacheKey);
    return timetableService.getWeeklyTimetable(request)
            .doOnSuccess(result -> {
              if (!result.isEmpty()) {
                redisTemplate.opsForValue().set(cacheKey, result, Duration.ofDays(1));
                log.info("NEIS API 응답 Redis 캐싱 완료 - key: {}", cacheKey);
              } else {
                log.warn("NEIS API 응답이 비어있음 - key: {}", cacheKey);
              }
            })
            .doOnError(error -> log.error("Fallback API 호출 실패 - key: {}", cacheKey, error));
  }

  private String buildCacheKey(TimetableRequest request, String from, String to) {
    return String.format("timetable:%s:%s:%s:%s", request.getGrade(), request.getClassNumber(), from, to);
  }
}