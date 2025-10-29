package hello.cluebackend.presentation.api.classroom;

import hello.cluebackend.application.classroom.dto.ClassRoomAllInfoDto;
import hello.cluebackend.application.classroom.dto.ClassRoomCardDto;
import hello.cluebackend.application.classroom.dto.ClassRoomDto;
import hello.cluebackend.domain.classroom.service.ClassRoomQueryService;
import hello.cluebackend.application.user.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassRoomQueryController {
    private final ClassRoomQueryService classRoomQueryService;

    @GetMapping
    public ResponseEntity<List<ClassRoomCardDto>> getAllClassRooms(@AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        return ResponseEntity.ok(classRoomQueryService.findMyClassRoomById(customOAuth2User.getUserId()));
    }

    @GetMapping("/{classId}/all")
    public ResponseEntity<ClassRoomAllInfoDto> getAllInfo(
            @PathVariable UUID classId
    ){
        return ResponseEntity.ok(classRoomQueryService.getAllInfo(classId));
    }

    @GetMapping("/{classId}")
    public ResponseEntity<ClassRoomDto> findClassRoom(
            @PathVariable UUID classId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
      ClassRoomDto findClassRoomDto = classRoomQueryService.findById(customOAuth2User.getUserId(), classId);
      return ResponseEntity.ok(findClassRoomDto);
    }
}