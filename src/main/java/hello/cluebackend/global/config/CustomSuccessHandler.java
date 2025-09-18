package hello.cluebackend.global.config;

import hello.cluebackend.domain.user.presentation.dto.CustomOAuth2User;
import hello.cluebackend.domain.user.presentation.dto.UserDto;
import hello.cluebackend.global.security.jwt.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${front.base-url}")
    private String baseurl;

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

        UserDto userDTO = customUserDetails.getUserDTO();

        int classCode = userDTO.getClassCode();
        if (classCode == -1) {
            request.getSession().setAttribute("firstUser", userDTO);
            getRedirectStrategy().sendRedirect(
                    request,
                    response,
                    baseurl + "/register"
            );
        } else {
            String username = customUserDetails.getUsername();
            UUID userId = customUserDetails.getUserId();

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            GrantedAuthority auth = iterator.next();
            String role = auth.getAuthority();

            String access = jwtUtil.createJwt("access", userId, username, role, 60*60*1000L);
            String refresh = jwtUtil.createJwt("refresh", userId, username, role,7 * 24  * 60 * 60 * 1000L);

            refreshTokenService.saveRefreshToken(refresh, username);

            String body = "{"
                    + "\"access_token\":\"Bearer " + access + "\","
                    + "\"refresh_token\":\"" + refresh + "\""
                    + "}";


            response.setHeader("Authorization", "Bearer " + access);
            response.addCookie(createCookie("refresh_token", refresh));
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(body);
            response.sendRedirect(baseurl);
        }
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(7 * 24  * 60 * 60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
