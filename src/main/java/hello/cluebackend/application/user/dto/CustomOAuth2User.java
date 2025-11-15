package hello.cluebackend.application.user.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class CustomOAuth2User implements OAuth2User, UserDetails {
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
                return "ROLE_" + userDTO.getRole().name();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return userDTO.getUsername();
    }

    @Override
    public String getUsername() {
        return userDTO.getUsername();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UUID getUserId() {
        return userDTO.getUserId();
    }

    public int getClassCode() {
        return userDTO.getClassCode();
    }
}
