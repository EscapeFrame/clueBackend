package hello.cluebackend.domain.user.presentation.dto;

import hello.cluebackend.domain.user.domain.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long userId;
    private String email;
    private Role role;
    private String username;
    private int classCode;
    private String addition;

    public UserDTO(Long userId, String email, Role role, String username, int classCode) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.username = username;
        this.classCode = classCode;
    }

    public UserDTO() {}
}