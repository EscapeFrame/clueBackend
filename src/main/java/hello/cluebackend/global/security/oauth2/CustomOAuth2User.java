package hello.cluebackend.global.security.oauth2;

import hello.cluebackend.domain.user.presentation.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private final UserDTO userDTO;

    public CustomOAuth2User(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public UserDTO getUserDTO() {
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
}
