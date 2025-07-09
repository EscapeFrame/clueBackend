package hello.cluebackend.domain.user.presentation;

import hello.cluebackend.domain.user.presentation.dto.DefaultRegisterUserDTO;
import hello.cluebackend.domain.user.presentation.dto.RegisterUserDTO;
import hello.cluebackend.domain.user.presentation.dto.UserDTO;
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
    public String processRegistration(RegisterUserDTO registerUserDTO) {
        return "redirect:/";
    }
    @PostMapping("/first-register")
    public UserDTO showRegistrationForm(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO dto = (UserDTO) session.getAttribute("firstUser");
        session.removeAttribute("firstUser");
        return dto;
    }

    @PostMapping(
            value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> processRegistration(@RequestBody DefaultRegisterUserDTO defaultRegisterUserDTO) {
        System.out.println("StudentID 1 : " + defaultRegisterUserDTO.getStudentId());
        registerUserService.registerUser(defaultRegisterUserDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
