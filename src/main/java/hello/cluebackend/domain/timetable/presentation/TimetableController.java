package hello.cluebackend.domain.timetable.presentation;

import hello.cluebackend.domain.timetable.presentation.dto.request.TimetableRequestDto;
import hello.cluebackend.domain.timetable.presentation.dto.response.TimetableResponseDto;
import hello.cluebackend.domain.timetable.service.TimetableService;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import hello.cluebackend.domain.user.presentation.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timetable")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TimetableController {
  private final TimetableService timetableService;

  @GetMapping("/today")
  public Mono<ResponseEntity<List<TimetableResponseDto>>> getTodayTimetable(@RequestParam TimetableRequestDto request) {
    return timetableService.getTodayTimetable(request)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.noContent().build())
            .onErrorReturn(ResponseEntity.internalServerError().build());
  }

  @GetMapping("/weekly")
  public Mono<ResponseEntity<List<TimetableResponseDto>>> getWeeklyTimetable(@RequestParam TimetableRequestDto request) {
    return timetableService.getWeeklyTimetable(request)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.noContent().build())
            .onErrorReturn(ResponseEntity.internalServerError().build());
  }
}