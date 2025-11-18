package hello.cluebackend.presentation.api.directory;

import hello.cluebackend.application.directory.dto.RequestDirectoryDto;
import hello.cluebackend.domain.directory.service.DirectoryService;
import hello.cluebackend.domain.user.model.Role;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.application.user.dto.oauth2.CustomOAuth2User;
import hello.cluebackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/directory")
@RequiredArgsConstructor
public class DirectoryController {
    private final DirectoryService directoryService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createDirectory(
            @RequestBody RequestDirectoryDto requestDirectoryDto,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
        UserEntity user = userService.findById(customOAuth2User.getUserId());
        if(!user.getRole().equals(Role.TEACHER)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        directoryService.createDirectory(requestDirectoryDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> updateDirectory(
            @RequestBody RequestDirectoryDto requestDirectoryDto,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ){
      UserEntity user = userService.findById(customOAuth2User.getUserId());
      if(!user.getRole().equals(Role.TEACHER)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      directoryService.updateDirectory(requestDirectoryDto);
      return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDirectory(
            @RequestBody RequestDirectoryDto requestDirectoryDto,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
      UserEntity user = userService.findById(customOAuth2User.getUserId());
      if(!user.getRole().equals(Role.TEACHER)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      directoryService.deleteById(requestDirectoryDto.getDirectoryId());
      return new ResponseEntity<>(HttpStatus.OK);
    }
}
