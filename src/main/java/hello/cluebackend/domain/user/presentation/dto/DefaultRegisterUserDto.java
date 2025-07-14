package hello.cluebackend.domain.user.presentation.dto;

import hello.cluebackend.domain.user.domain.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultRegisterUserDto {
    private String email;
    private String username;
    private int classCode;
    private Role role;
}
