package hello.cluebackend.domain.user.service;


import hello.cluebackend.application.user.dto.CustomOAuth2User;
import hello.cluebackend.application.user.dto.GoogleResponse;
import hello.cluebackend.application.user.dto.OAuth2Response;
import hello.cluebackend.application.user.dto.UserDto;
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
        username = username.substring(2);

        Optional<UserEntity> existDataOptional = userJpaRepository.findByEmail(email);

        if(existDataOptional.isEmpty()) {
            UserDto userDTO = new UserDto();
            userDTO.setEmail(oAuth2Response.getEmail());
            userDTO.setUsername(username);
            userDTO.setRole(role);
            userDTO.setClassCode(-1);

            return new CustomOAuth2User(userDTO);
        }

        UserEntity existData = existDataOptional.get();
        existData.setEmail(oAuth2Response.getEmail());
        existData.setUsername(oAuth2Response.getName());
        userJpaRepository.save(existData);

        UserDto userDTO = existData.toUserDTO();
        return new CustomOAuth2User(userDTO);
    }

}