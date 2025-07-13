package hello.cluebackend.domain.classroom.presentation.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomCardDto {
    private Long classRoomId;
    private String name;
    private String sort;
    private String target;
    private int studentCount;
    private boolean isActivation;
}
