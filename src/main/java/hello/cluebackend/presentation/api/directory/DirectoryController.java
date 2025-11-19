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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/directory")
@RequiredArgsConstructor
public class DirectoryController {
    private final DirectoryService directoryService;
    private final UserService userService;

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public ResponseEntity<?> createDirectory(@RequestBody RequestDirectoryDto requestDirectoryDto) {
        directoryService.createDirectory(requestDirectoryDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PatchMapping
    public ResponseEntity<Void> updateDirectory(@RequestBody RequestDirectoryDto requestDirectoryDto) {
      directoryService.updateDirectory(requestDirectoryDto);
      return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping
    public ResponseEntity<Void> deleteDirectory(@RequestBody RequestDirectoryDto requestDirectoryDto) {
      directoryService.deleteById(requestDirectoryDto.getDirectoryId());
      return ResponseEntity.status(HttpStatus.OK).build();
    }
}
