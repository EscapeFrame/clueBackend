package hello.cluebackend.domain.classroom.controller;

import hello.cluebackend.domain.classroom.controller.dto.ClassRoomAllInfoDto;
import hello.cluebackend.domain.classroom.controller.dto.ClassRoomCardDto;
import hello.cluebackend.domain.classroom.controller.dto.ClassRoomDto;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.presentation.dto.CustomOAuth2User;
import hello.cluebackend.domain.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassRoomController {
    private final ClassRoomService classRoomService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<ClassRoomCardDto>> getAllClassRooms(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        return ResponseEntity.ok(classRoomService.findMyClassRoomById(customOAuth2User.getUserId()));
    }

    @GetMapping("/{classId}/all")
    public ResponseEntity<ClassRoomAllInfoDto> getAllInfo(@PathVariable UUID classId){
        return ResponseEntity.ok(classRoomService.getAllInfo(classId));
    }

    @PostMapping
    public ResponseEntity<HashMap<?,?>> createClassRoom(
            @RequestBody ClassRoomDto classRoomDTO,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
      if(customOAuth2User.getUserDTO().getRole() != Role.TEACHER) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      try {
          classRoomService.createClassRoom(classRoomDTO, customOAuth2User.getUserId());
          return new ResponseEntity<>(HttpStatus.OK);
      } catch (EntityNotFoundException e){
          log.error(e.getMessage());
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }

    @PostMapping("/{code}/members")
    public ResponseEntity<?> joinClassRoom(
            @PathVariable String code,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
      try {
          classRoomService.joinClassRoom(customOAuth2User.getUserId(), code);
          return ResponseEntity.ok().build();
      } catch (IllegalArgumentException e){
          log.debug(e.getMessage());
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }

    @GetMapping("/{classId}")
    public ResponseEntity<ClassRoomDto> findClassRoom(
            @PathVariable UUID classId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
//        if(customOAuth2User.getUserDTO().getRole != Role.TEACHER) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }

        ClassRoomDto findClassRoomDto = classRoomService.findById(classId);
        return ResponseEntity.ok(findClassRoomDto);
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

        try {
            classRoomService.updateClassRoom(classId, classRoomDTO);
        } catch (IllegalArgumentException e){
            log.debug(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().build();
    }
}