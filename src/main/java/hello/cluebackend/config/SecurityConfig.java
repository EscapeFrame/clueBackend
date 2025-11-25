package hello.cluebackend.config;

import hello.cluebackend.domain.user.service.CustomOAuth2UserService;
import hello.cluebackend.common.utils.JWTUtil;
import hello.cluebackend.infrastructure.security.jwt.RefreshTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                          CustomSuccessHandler customSuccessHandler,
                          JWTUtil jwtUtil,
                          RefreshTokenService refreshTokenService) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @Bean
    @Order(0)
    public SecurityFilterChain loginChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/oauth2/**", "/login/oauth2/**", "/first-register", "/register", "/api/timetable/**")
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/oauth2/**", "/login/oauth2/**", "/first-register", "/register", "/api/timetable/**")
                        .permitAll()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new ClientTypeFilter(), org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.class)
                .oauth2Login(o -> o
                        .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                )
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(a -> a
                        .requestMatchers(
                                "/", "/reissue", "/h2-console/**",
                                "/favicon.ico", "/error",
                                "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**",
                                "/test",
                                "/ws-quiz/**", "/ws-quiz-raw/**",
                                "/topic/**", "/queue/**", "/app/**",
                                "/ui/**"
                        ).permitAll()
//                        .requestMatchers("/api/notice").hasRole(Role.TEACHER.name())
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
