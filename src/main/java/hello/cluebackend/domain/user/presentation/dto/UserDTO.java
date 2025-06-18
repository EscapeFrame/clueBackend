package hello.cluebackend.domain.user.presentation.dto;

import hello.cluebackend.domain.user.domain.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String email;
    private Role role;
    private String username;
    private int studentId;
    private String addition;

    public UserDTO(String email, Role role, String username, int studentId, String addition) {
        this.email = email;
        this.role = role;
        this.username = username;
        this.studentId = studentId;
        this.addition = addition;
    }

    public UserDTO() {}
}