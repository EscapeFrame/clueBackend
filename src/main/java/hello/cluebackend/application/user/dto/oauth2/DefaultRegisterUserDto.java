package hello.cluebackend.application.user.dto.oauth2;

import hello.cluebackend.domain.user.model.Role;
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
