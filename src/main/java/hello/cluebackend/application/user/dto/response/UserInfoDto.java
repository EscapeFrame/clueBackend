package hello.cluebackend.application.user.dto.response;

import hello.cluebackend.domain.user.model.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {
    private UUID userId;
    private String email;
    private Role role;
    private String username;
    private int grade;
    private int classNo;
    private int number;
    private LocalDateTime createdAt;
}
