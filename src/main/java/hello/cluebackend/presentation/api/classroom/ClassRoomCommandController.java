package hello.cluebackend.presentation.api.classroom;

import hello.cluebackend.application.classroom.dto.ClassRoomDto;
import hello.cluebackend.domain.classroom.service.ClassRoomCommandService;
import hello.cluebackend.application.user.dto.CustomOAuth2User;
import hello.cluebackend.domain.user.model.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassRoomCommandController {
  private final ClassRoomCommandService classRoomCommandService;

  @PostMapping
  public ResponseEntity<HashMap<?,?>> createClassRoom(
          @RequestBody ClassRoomDto classRoomDTO,
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User
  ) {
    if(customOAuth2User.getUserDTO().getRole() != Role.TEACHER) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    classRoomCommandService.createClassRoom(classRoomDTO, customOAuth2User.getUserId());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/{code}/members")
  public ResponseEntity<?> joinClassRoom(
          @PathVariable String code,
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User
  ) {
    classRoomCommandService.joinClassRoom(customOAuth2User.getUserId(), code);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/{classId}")
  public ResponseEntity<?> updateClassRoom(
          @PathVariable UUID classId,
          @RequestBody ClassRoomDto classRoomDTO,
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User
  ) {
    if(customOAuth2User.getUserDTO().getRole() != Role.TEACHER) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    classRoomCommandService.updateClassRoom(classId, customOAuth2User.getUserId(), classRoomDTO);

    return ResponseEntity.ok().build();
  }


  @DeleteMapping("/{classId}")
  public ResponseEntity<?> deleteClassRoom(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID classId
  ) {
    classRoomCommandService.deleteClassRoom(customOAuth2User.getUserId(), classId);
    return ResponseEntity.noContent().build();
  }
}
