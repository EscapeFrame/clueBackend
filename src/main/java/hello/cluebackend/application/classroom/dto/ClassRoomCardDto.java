package hello.cluebackend.application.classroom.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomCardDto {
    private UUID classRoomId;
    private String name;
    private String sort;
    private String target;
    private int studentCount;
    private boolean isActivation;
}
