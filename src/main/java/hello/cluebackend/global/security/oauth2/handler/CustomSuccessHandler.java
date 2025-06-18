package hello.cluebackend.global.security.oauth2.handler;

import hello.cluebackend.global.security.jwt.JWTUtil;
import hello.cluebackend.domain.user.presentation.dto.UserDTO;
import hello.cluebackend.global.security.jwt.service.RefreshTokenService;
import hello.cluebackend.global.security.oauth2.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    public CustomSuccessHandler(JWTUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        UserDTO userDTO = customUserDetails.getUserDTO();

        int studentId = userDTO.getStudentId();
        if (studentId == -1) {
            request.getSession().setAttribute("firstUser", userDTO);
            getRedirectStrategy().sendRedirect(
                    request,
                    response,
                    "http://localhost:3000/register"
            );
        } else {
            String username = customUserDetails.getUsername();

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            GrantedAuthority auth = iterator.next();
            String role = auth.getAuthority();
            String access = jwtUtil.createJwt("access", username, role, 60*60*1000L);
            String refresh = jwtUtil.createJwt("refresh", username, role,7 * 24  * 60 * 60 * 1000L);

            refreshTokenService.saveRefreshToken(refresh, username);
            response.setHeader("Authorization", "Bearer " + access);
            response.addCookie(createCookie("refresh_token", refresh));
            response.setStatus(HttpStatus.OK.value());
            response.sendRedirect("http://localhost:3000/");
        }
    }



    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(7 * 24  * 60 * 60);
        // cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
