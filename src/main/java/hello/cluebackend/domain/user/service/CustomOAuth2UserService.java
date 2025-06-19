package hello.cluebackend.domain.user.service;


import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import hello.cluebackend.domain.user.presentation.dto.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String email = oAuth2User.getAttribute("email");
        String isBssm = email.split("@")[1];
        String isTeacher = email.split("@")[0];
        if(!isBssm.equals("bssm.hs.kr")) {
            System.out.println("not bssm");
            throw new IllegalArgumentException("not bssm email");
        }

        Role role;
        if(isTeacher.equals("teacher")) role = Role.TEACHER;
        else role = Role.STUDENT;

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        String username = oAuth2Response.getName();
        username = username.substring(2);

        Optional<UserEntity> existDataOptional = userRepository.findByEmail(email);

        if(existDataOptional.isEmpty()) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(oAuth2Response.getEmail());
            userDTO.setUsername(username);
            userDTO.setRole(role);
            userDTO.setStudentId(-1);

            return new CustomOAuth2User(userDTO);
        }

        UserEntity existData = existDataOptional.get();
        existData.setEmail(oAuth2Response.getEmail());
        existData.setUsername(oAuth2Response.getName());
        userRepository.save(existData);

        UserDTO userDTO = existData.toUserDTO();
        return new CustomOAuth2User(userDTO);
    }

}
