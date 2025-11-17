package hello.cluebackend.application.user.dto.response;

import hello.cluebackend.domain.user.model.Role;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDataDto {
    private UUID userId;
    private String username;
    private Role role;
    private Integer classCode;
}
