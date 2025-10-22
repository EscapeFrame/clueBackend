package hello.cluebackend.domain.user.controller.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class CustomOAuth2User implements OAuth2User {
    private final UserDto userDTO;

    public CustomOAuth2User(UserDto userDTO) {
        this.userDTO = userDTO;
    }

    public UserDto getUserDTO() {
        return userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.getRole().name();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return userDTO.getUsername();
    }

    public String getUsername() {
        return userDTO.getUsername();
    }

    public UUID getUserId() {
        return userDTO.getUserId();
    }

    public int getClassCode() {
        return userDTO.getClassCode();
    }
}
