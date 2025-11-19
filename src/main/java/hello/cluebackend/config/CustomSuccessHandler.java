package hello.cluebackend.config;

import hello.cluebackend.application.user.dto.oauth2.CustomOAuth2User;
import hello.cluebackend.application.user.dto.response.UserDto;
import hello.cluebackend.infrastructure.security.jwt.RefreshTokenService;
import hello.cluebackend.common.utils.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    private String frontBaseUrl;

    @Value("${app.base-url}")
    private String appBaseUrl;

    @Value("${app.redirect-url.login}")
    private String appLoginRedirectUrl;

    @Value("${app.redirect-url.register}")
    private String appRegisterRedirectUrl;

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

        UserDto userDto = customUserDetails.getUserDTO();

        String baseUrl = frontBaseUrl;

        HttpSession session = request.getSession(false); // 세션이 없을 수도 있으면 false로
        String clientType = null;
        if (session != null) {
            Object clientTypeObj = session.getAttribute("client_type");
            clientType = clientTypeObj != null ? clientTypeObj.toString() : null;

            if ("app".equals(clientType)) {
                baseUrl = appBaseUrl;
            }

            // 확인 후 세션에서 삭제
            session.removeAttribute("client_type");
        }
        System.out.println("SUCCESS!!! baseUrl: " + baseUrl);
        int grade = userDto.getGrade();
        int classNo = userDto.getClassNo();
        int number = userDto.getNumber();
        if (grade == -1 ||  classNo == -1 || number == -1) {
            request.getSession().setAttribute("firstUser", userDto);

            if ("app".equals(clientType)) {
                baseUrl = baseUrl+ appRegisterRedirectUrl ;
            } else {
                baseUrl = baseUrl + "/register";
            }
            getRedirectStrategy().sendRedirect(request, response, baseUrl);
        } else {
            String username = customUserDetails.getUsername();
            UUID userId = customUserDetails.getUserId();
            String email =  userDto.getEmail();

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
            GrantedAuthority auth = iterator.next();
            String role = String.valueOf(customUserDetails.getUserDTO().getRole());

            String access = jwtUtil.createJwt("access", userId, username, email, role, 60*60*1000L);
            String refresh = jwtUtil.createJwt("refresh", userId, username, email, role,7 * 24  * 60 * 60 * 1000L);

            refreshTokenService.saveRefreshToken(refresh, username);

//            response.setHeader("Authorization", "Bearer " + access);
            response.addCookie(createCookie("refresh_token", refresh));
            response.setStatus(HttpStatus.OK.value());
            if ("app".equals(clientType)) {
                baseUrl = baseUrl+ appLoginRedirectUrl + "?access_token=" + access;
            } else {
                baseUrl = baseUrl+"/login?access_token=" + access;
            }
//            response.sendRedirect(baseUrl);
            getRedirectStrategy().sendRedirect(request, response, baseUrl);
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
