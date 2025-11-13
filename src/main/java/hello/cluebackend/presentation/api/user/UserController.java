package hello.cluebackend.presentation.api.user;

import hello.cluebackend.application.user.dto.*;
import hello.cluebackend.domain.user.model.Role;
import hello.cluebackend.domain.user.service.UserService;
import hello.cluebackend.common.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> processRegistration(HttpServletRequest request,
                                                 RegisterUserDto registerUserDto) {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("firstUser");
        session.removeAttribute("firstUser");
        log.info("ClassCode 1 : " + registerUserDto.getClassCode());
        userService.registerUser(userDto, registerUserDto.getClassCode());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/api/user/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal CustomOAuth2User customUser) {
        return ResponseEntity.status(HttpStatus.OK).body(customUser.getUserDTO());
    }

    @PostMapping("/test")
    public ResponseEntity<?> issueToken(@RequestParam UUID userId, @RequestParam String username, @RequestParam String email, @RequestParam String role, HttpServletResponse response) {
        String access = jwtUtil.createJwt("access", userId, username, email, role, 100 * 60 * 60 * 1000L);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + access)
                .body("JWT access token and refresh token issued for dev use.");
    }
}