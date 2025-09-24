package hello.cluebackend.domain.user.presentation;

import hello.cluebackend.global.utils.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final JWTUtil jwtUtil;

    @PostMapping("/test")
    public ResponseEntity<?> issueToken(@RequestParam UUID userId, @RequestParam String username, @RequestParam String role, HttpServletResponse response) {
        String access = jwtUtil.createJwt("access", userId, username, role, 100 * 60 * 60 * 1000L);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + access)
                .body("JWT access token and refresh token issued for dev use.");
    }
}