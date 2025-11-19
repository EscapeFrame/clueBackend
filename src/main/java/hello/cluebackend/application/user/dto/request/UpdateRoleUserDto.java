package hello.cluebackend.application.user.dto.request;

import hello.cluebackend.domain.user.model.Role;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleUserDto {
    private Role role;
    private UUID userId;
}
