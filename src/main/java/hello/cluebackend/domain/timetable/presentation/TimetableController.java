package hello.cluebackend.domain.timetable.presentation;

import hello.cluebackend.domain.timetable.presentation.dto.request.TimetableRequestDto;
import hello.cluebackend.domain.timetable.presentation.dto.response.TimetableResponseDto;
import hello.cluebackend.domain.timetable.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
public class TimetableController {
  private final TimetableService timetableService;

  @GetMapping("/test")
  public String test(){
    return "Test";
  }

  @GetMapping("/today")
  public Mono<ResponseEntity<List<TimetableResponseDto>>> getTodayTimetable(
          @RequestParam(required = true) String grade,
          @RequestParam(required = true) String classNumber
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