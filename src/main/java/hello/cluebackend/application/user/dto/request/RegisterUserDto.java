package hello.cluebackend.application.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private int grade;
    private int classNo;
    private int number;
    private String username;
}
