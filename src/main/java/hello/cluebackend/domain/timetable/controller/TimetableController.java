package hello.cluebackend.domain.timetable.controller;

import hello.cluebackend.domain.timetable.service.CacheTimetableService;
import hello.cluebackend.domain.timetable.controller.dto.request.TimetableRequest;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.service.UserService;
import hello.cluebackend.global.common.annotation.CurrentUser;
import hello.cluebackend.global.utils.ClassCodeUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
public class TimetableController {

  private final CacheTimetableService cacheTimetableService;
  private final UserService userService;

  @GetMapping("/today")
  public Mono<ResponseEntity<List<?>>> getTodayTimetable(@CurrentUser UUID userId) {
    UserEntity user = userService.findById(userId).toEntity();
    String[] codes = ClassCodeUtils.classCodeConverter(user);

    TimetableRequest request = new TimetableRequest(codes[0], codes[1]);

    return cacheTimetableService.getTodayTimetableWithCache(request)
            .map(list -> ResponseEntity.ok(list.isEmpty() ? List.of() : list))
            .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body(List.of())));
  }

  @GetMapping("/weekly")
  public Mono<ResponseEntity<List<?>>> getWeeklyTimetable(@CurrentUser UUID userId) {
    UserEntity user = userService.findById(userId).toEntity();
    String[] codes = ClassCodeUtils.classCodeConverter(user);

    TimetableRequest request = new TimetableRequest(codes[0], codes[1]);

    return cacheTimetableService.getWeeklyTimetableWithCache(request)
            .map(list -> ResponseEntity.ok(list.isEmpty() ? List.of() : list))
            .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body(List.of())));
  }


}