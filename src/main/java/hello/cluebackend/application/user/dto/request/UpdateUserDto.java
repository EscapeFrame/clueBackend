package hello.cluebackend.application.user.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDto {
    private String username;
    private String description;
    private int grade;
    private int classNo;
    private int number;
}
