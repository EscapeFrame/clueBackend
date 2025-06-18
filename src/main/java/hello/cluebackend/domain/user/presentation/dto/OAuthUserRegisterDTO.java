package hello.cluebackend.domain.user.presentation.dto;

import hello.cluebackend.domain.user.domain.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthUserRegisterDTO {
    private String email;
    private String username;
    private int studentId;
    private Role role;
}
