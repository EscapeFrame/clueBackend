package hello.cluebackend.domain.timetable.presentation;

import hello.cluebackend.domain.timetable.presentation.dto.request.TimetableRequestDto;
import hello.cluebackend.domain.timetable.presentation.dto.response.TimetableResponseDto;
import hello.cluebackend.domain.timetable.service.TimetableService;
import lombok.RequiredArgsConstructor;

import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
public class TimetableController {
  private final TimetableService timetableService;

  @GetMapping("/today")
  public Mono<ResponseEntity<List<TimetableResponseDto>>> getTodayTimetable(
          @RequestParam(required = true) @Pattern(regexp = "^[1-3]$", message="학년은 1~3 사이여야 합니다.") String grade,
          @RequestParam(required = true) @Pattern(regexp = "^[1-4]$", message="반은 1~4 사이여야 합니다.") String classNumber
) {
    TimetableRequestDto request = new TimetableRequestDto(grade, classNumber);
    return timetableService.getTodayTimetable(request)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.noContent().build())
            .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().build()));
  }

  @GetMapping("/weekly")
  public Mono<ResponseEntity<List<TimetableResponseDto>>> getWeeklyTimetable(
          @RequestParam(required = true) String grade,
          @RequestParam(required = true) String classNumber
  ) {
    TimetableRequestDto request = new TimetableRequestDto(grade, classNumber);
    return timetableService.getWeeklyTimetable(request)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.noContent().build())
            .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().build()));
  }
}