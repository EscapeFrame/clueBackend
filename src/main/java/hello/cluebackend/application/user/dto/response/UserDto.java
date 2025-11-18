package hello.cluebackend.application.user.dto.response;

import hello.cluebackend.domain.user.model.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID userId;
    private String email;
    private Role role;
    private String username;
    private String description;
    private int classCode;
    private int grade;
    private int classNo;
    private int number;
    private LocalDateTime createdAt;

    public static UserDto first(String email, String username, Role role) {
        UserDto userDto = new UserDto();
        userDto.email = email;
        userDto.username = username;
        userDto.role = role;
        userDto.grade = -1;
        userDto.classNo = -1;
        userDto.number = -1;
        return userDto;
    }
}