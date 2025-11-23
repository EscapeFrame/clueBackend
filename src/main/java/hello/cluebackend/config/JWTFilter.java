package hello.cluebackend.config;

import hello.cluebackend.domain.user.model.Role;
import hello.cluebackend.application.user.dto.oauth2.CustomOAuth2User;
import hello.cluebackend.application.user.dto.response.UserDto;
import hello.cluebackend.common.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private static final AntPathRequestMatcher REFRESH_MATCHER = new AntPathRequestMatcher("/refresh-token", "POST");
    private static final AntPathRequestMatcher STATIC_MATCHER = new AntPathRequestMatcher("/ui/**");
    private static final AntPathRequestMatcher WS_MATCHER = new AntPathRequestMatcher("/ws-quiz/**");
    private static final AntPathRequestMatcher WS_RAW_MATCHER = new AntPathRequestMatcher("/ws-quiz-raw/**");
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Skip JWT filter for static resources and WebSocket endpoints
        if (REFRESH_MATCHER.matches(request) ||
            STATIC_MATCHER.matches(request) ||
            WS_MATCHER.matches(request) ||
            WS_RAW_MATCHER.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorization.substring(7);

        if(jwtUtil.isExpired(accessToken)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(accessToken);
        if(!category.equals("access")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = jwtUtil.getUsername(accessToken);
        Role role = jwtUtil.getRole(accessToken);
        UUID userId = jwtUtil.getUserId(accessToken);
        String email = jwtUtil.getEmail(accessToken);

        UserDto userDTO = new UserDto();
        userDTO.setUsername(username);
        userDTO.setRole(role);
        userDTO.setUserId(userId);
        userDTO.setEmail(email);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        for(GrantedAuthority grantedAuthority : customOAuth2User.getAuthorities()) {
            log.info("Role : {}", grantedAuthority.getAuthority());
        }


        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
