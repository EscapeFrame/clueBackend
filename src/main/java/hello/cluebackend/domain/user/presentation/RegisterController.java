package hello.cluebackend.domain.user.presentation;

import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.presentation.dto.DefaultRegisterUserDto;
import hello.cluebackend.domain.user.presentation.dto.RegisterUserDto;
import hello.cluebackend.domain.user.presentation.dto.UserDto;
import hello.cluebackend.domain.user.service.UserService;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RegisterController {

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

    @GetMapping("/me")
    public ResponseEntity<userData> getCurrentUser(HttpServletRequest request) {
        String token = jwtUtil.getToken(request);
        UUID userId = jwtUtil.getUserId(token);
        Role role = jwtUtil.getRole(token);
        String username = jwtUtil.getUsername(token);

        return ResponseEntity.status(HttpStatus.OK).body(new userData(userId, username, role));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class userData {
        private UUID userId;
        private String username;
        private Role role;
    }
}