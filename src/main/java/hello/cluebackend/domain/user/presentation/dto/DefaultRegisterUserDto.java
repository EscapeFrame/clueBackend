package hello.cluebackend.domain.user.presentation.dto;

import hello.cluebackend.domain.user.domain.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultRegisterUserDto {
    private String email;
    private String username;
    private Role role;
}
