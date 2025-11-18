package hello.cluebackend.domain.user.service;


import hello.cluebackend.application.user.dto.oauth2.CustomOAuth2User;
import hello.cluebackend.application.user.dto.oauth2.GoogleResponse;
import hello.cluebackend.application.user.dto.oauth2.OAuth2Response;
import hello.cluebackend.application.user.dto.response.UserDto;
import hello.cluebackend.domain.user.model.Role;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.infrastructure.persistence.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserJpaRepository userJpaRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String email = oAuth2User.getAttribute("email");
        String isTeacher = email.split("@")[0];

        Role role;
        if(isTeacher.equals("teacher")) role = Role.TEACHER;
        else role = Role.STUDENT;

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        String username = oAuth2Response.getName();

        Optional<UserEntity> existDataOptional = userJpaRepository.findByEmail(email);

        if(existDataOptional.isEmpty()) {
            UserDto userDto = UserDto.first(oAuth2Response.getEmail(), username, role);
            return new CustomOAuth2User(userDto);
        }

        UserEntity existData = existDataOptional.get();
        existData.update(oAuth2Response.getEmail(), username);
        userJpaRepository.save(existData);

        UserDto userDto = existData.toUserDTO();
        return new CustomOAuth2User(userDto);
    }

}