package hello.cluebackend.infrastructure.security.jwt;

import hello.cluebackend.common.utils.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void saveRefreshToken(String refreshToken, String username) {
        refreshTokenRepository.save(new RefreshToken(refreshToken, username));
        System.out.println(refreshTokenRepository.existsById(refreshToken));
    }

    public boolean existsByRefresh(String refreshToken) {
        return refreshTokenRepository.existsById(refreshToken);
    }

    public void reissueRefreshToken(HttpServletRequest request, HttpServletResponse response) throws AuthenticationCredentialsNotFoundException {
//        System.out.println("reissueRefreshToken");
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthenticationCredentialsNotFoundException("refresh token null");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh_token")) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            throw new AuthenticationCredentialsNotFoundException("refresh token null");
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e){
            throw new AuthenticationCredentialsNotFoundException("refresh token expired");
        }

        String category = jwtUtil.getCategory(refreshToken);

        if (!"refresh".equals(category)) {
            throw new AuthenticationCredentialsNotFoundException("Invalid refresh token");
        }

        if (!existsByRefresh(refreshToken)) {
            throw new AuthenticationCredentialsNotFoundException("Invalid refresh token");
        }

        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken).name();
        UUID userId = jwtUtil.getUserId(refreshToken);


        String newAccessToken = jwtUtil.createJwt("access", userId, username, role, 60 * 10 * 1000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", userId, username, role,24 * 60 * 60 * 1000L);

        saveRefreshToken(newRefreshToken, username);
        deleteByRefresh(refreshToken);

        response.setHeader("Authorization", "Bearer " + newAccessToken);
        response.addCookie(createCookie("refresh_token", newRefreshToken));
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(7 * 24  * 60 * 60);
        // cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    public void deleteByRefresh(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }
}
