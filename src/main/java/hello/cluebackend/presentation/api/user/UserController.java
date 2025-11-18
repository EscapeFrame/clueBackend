package hello.cluebackend.presentation.api.user;

import hello.cluebackend.application.user.dto.oauth2.CustomOAuth2User;
import hello.cluebackend.application.user.dto.oauth2.DefaultRegisterUserDto;
import hello.cluebackend.application.user.dto.request.RegisterUserDto;
import hello.cluebackend.application.user.dto.request.UpdateUserDto;
import hello.cluebackend.application.user.dto.response.UserDto;
import hello.cluebackend.application.user.dto.response.UserImage;
import hello.cluebackend.domain.user.service.UserService;
import hello.cluebackend.common.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final JWTUtil jwtUtil;
    private final UserService userService;

    @GetMapping("/first-register")
    public DefaultRegisterUserDto showRegistrationForm(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("firstUser");
        return DefaultRegisterUserDto.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> processRegistration(
            HttpServletRequest request,
            @RequestPart(value = "user") RegisterUserDto registerUserDto,
            @RequestPart(value = "image") MultipartFile image) throws IOException {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("firstUser");
        session.removeAttribute("firstUser");
        userService.registerUser(userDto, registerUserDto, image);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/api/user")
    public ResponseEntity<Void> updateProfile(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody UpdateUserDto userDto) {
        log.info("UpdateUserDto : {}", userDto);
        userService.updateProfile(customOAuth2User.getUserDTO().getUserId(), userDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/user/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal CustomOAuth2User customUser) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(customUser.getUserDTO().getUserId()));
    }

    @GetMapping("/api/user/me/image")
    public ResponseEntity<Resource> getImage(@AuthenticationPrincipal CustomOAuth2User customUser) {
        UserImage userImage = userService.getMyImage(customUser.getUserDTO().getUserId());

        String contentType = userImage.getContentType();
        MediaType mediaType = (contentType != null) ? MediaType.parseMediaType(contentType) : MediaType.ALL;

        return ResponseEntity.
                status(HttpStatus.OK)
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline()
                        .build()
                        .toString())
                .body(userImage.getResource());
    }

    @PutMapping("/api/user/me/image")
    public ResponseEntity<Void> updateImage(
            @AuthenticationPrincipal CustomOAuth2User customUser,
            MultipartFile image) throws IOException {
        userService.updateImage(customUser.getUserDTO().getUserId(), image);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/test")
    public ResponseEntity<?> issueToken(
            @RequestParam UUID userId,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String role) {
        String access = jwtUtil.createJwt("access", userId, username, email, role, 100 * 60 * 60 * 1000L);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + access)
                .body("JWT access token and refresh token issued for dev use.");
    }
}