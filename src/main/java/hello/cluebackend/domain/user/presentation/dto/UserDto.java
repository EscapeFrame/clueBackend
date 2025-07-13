package hello.cluebackend.domain.user.presentation.dto;

import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;
    private String email;
    private Role role;
    private String username;
    private int classCode;
    private LocalDateTime createdAt;

    public UserDto(Long userId, String email, Role role, String username, int classCode) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.username = username;
        this.classCode = classCode;
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .userId(userId)
                .email(email)
                .role(role)
                .username(username)
                .classCode(classCode)
                .createdAt(createdAt)
                .build();
    }
}