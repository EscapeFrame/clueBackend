package hello.cluebackend.domain.user.presentation;

import hello.cluebackend.domain.user.presentation.dto.DefaultRegisterUserDto;
import hello.cluebackend.domain.user.presentation.dto.RegisterUserDto;
import hello.cluebackend.domain.user.presentation.dto.UserDto;
import hello.cluebackend.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

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
}