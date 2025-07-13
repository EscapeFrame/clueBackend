package hello.cluebackend.domain.user.presentation;

import hello.cluebackend.domain.user.presentation.dto.DefaultRegisterUserDto;
import hello.cluebackend.domain.user.presentation.dto.RegisterUserDto;
import hello.cluebackend.domain.user.presentation.dto.UserDto;
import hello.cluebackend.domain.user.service.RegisterUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {
    private final RegisterUserService registerUserService;

    public RegisterController(RegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
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
        System.out.println("StudentID 1 : " + defaultRegisterUserDTO.getClassCode());
        registerUserService.registerUser(defaultRegisterUserDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
