package hello.cluebackend.domain.user.presentation;

import hello.cluebackend.domain.user.presentation.dto.DefaultRegisterUserDto;
import hello.cluebackend.domain.user.presentation.dto.RegisterUserDto;
import hello.cluebackend.domain.user.presentation.dto.UserDto;
import hello.cluebackend.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String processRegistration(RegisterUserDto registerUserDTO) {
        return "redirect:/";
    }

    @PostMapping("/first-register")
    public UserDto showRegistrationForm(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDto dto = (UserDto) session.getAttribute("firstUser");
        session.removeAttribute("firstUser");
        return dto;
    }

    @PostMapping(
            value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> processRegistration(@RequestBody DefaultRegisterUserDto defaultRegisterUserDTO) {
        log.info("ClassCode 1 : " + defaultRegisterUserDTO.getClassCode());
        userService.registerUser(defaultRegisterUserDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
