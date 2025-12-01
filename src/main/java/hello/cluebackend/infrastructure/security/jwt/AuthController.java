package hello.cluebackend.infrastructure.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AuthController {

    private final RefreshTokenService refreshTokenService;

    public AuthController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        refreshTokenService.reissueRefreshToken(request, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/app/reissue")
    public ResponseEntity<AppJwtToken> appRefreshToken(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(refreshTokenService.reissueRefreshToken(request));
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("logout request");
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            throw new AuthenticationCredentialsNotFoundException("refresh_token 쿠키가 존재하지 않습니다.");
        }

        refreshTokenService.deleteByRefresh(refreshToken);

        response.setHeader("Authorization", "Bearer ");

        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
