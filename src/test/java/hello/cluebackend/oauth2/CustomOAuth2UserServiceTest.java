package hello.cluebackend.oauth2;

import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import hello.cluebackend.domain.user.presentation.dto.CustomOAuth2User;
import hello.cluebackend.domain.user.service.CustomOAuth2UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Test
    void shouldThrowException_WhenEmailNotFromBSSM() {
        // given
        OAuth2UserRequest request = mockUserRequest();
        OAuth2User user = createOAuth2User("hacker@gmail.com", "teacher홍길동");

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            customOAuth2UserService.processOAuth2User(request, user);
        });
    }

    @Test
    void shouldReturnNewUserDTO_WhenFirstLogin() {
        // given
        OAuth2UserRequest request = mockUserRequest();
        OAuth2User user = createOAuth2User("student@bssm.hs.kr", "student김길동");

        when(userRepository.findByEmail("student@bssm.hs.kr")).thenReturn(Optional.empty());

        // when
        CustomOAuth2User result = (CustomOAuth2User) customOAuth2UserService.processOAuth2User(request, user);

        // then
        assertEquals("student@bssm.hs.kr", result.getUserDTO().getEmail());
        assertEquals(Role.STUDENT, result.getUserDTO().getRole());
        assertEquals(-1, result.getUserDTO().getStudentId());
    }

    @Test
    void shouldUpdateUser_WhenUserAlreadyExists() {
        // given
        OAuth2UserRequest request = mockUserRequest();
        OAuth2User user = createOAuth2User("teacher@bssm.hs.kr", "teacher김교사");

        UserEntity existing = new UserEntity();
        existing.setEmail("teacher@bssm.hs.kr");
        existing.setUsername("이전이름");
        existing.setRole(Role.TEACHER);
        existing.setStudentId(0);

        when(userRepository.findByEmail("teacher@bssm.hs.kr")).thenReturn(Optional.of(existing));

        // when
        CustomOAuth2User result = (CustomOAuth2User) customOAuth2UserService.processOAuth2User(request, user);

        // then
        assertEquals("teacher@bssm.hs.kr", result.getUserDTO().getEmail());
        assertEquals("teacher김교사", result.getUserDTO().getUsername());
    }

    // === 헬퍼 ===

    private OAuth2User createOAuth2User(String email, String name) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", email);
        attributes.put("name", name);

        return new DefaultOAuth2User(
                List.of(() -> "ROLE_USER"),
                attributes,
                "email"
        );
    }

    private OAuth2UserRequest mockUserRequest() {
        return new OAuth2UserRequest(
                ClientRegistration.withRegistrationId("google")
                        .clientId("dummy-client")
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .redirectUri("http://localhost/login/oauth2/code/google")
                        .authorizationUri("http://auth")
                        .tokenUri("http://token")
                        .userInfoUri("http://userinfo")
                        .userNameAttributeName("email")
                        .clientSecret("secret")
                        .build(),
                new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        "mock-token",
                        Instant.now(),
                        Instant.now().plusSeconds(3600)
                )
        );
    }
}
