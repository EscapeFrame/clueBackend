package hello.cluebackend.application.user.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateUserDto {
    private String username;
    private String description;
    private Integer grade;
    private Integer classNo;
    private Integer number;
}
