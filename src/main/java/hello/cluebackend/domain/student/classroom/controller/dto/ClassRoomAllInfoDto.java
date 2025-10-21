package hello.cluebackend.domain.student.classroom.controller.dto;

import hello.cluebackend.domain.student.directory.controller.dto.DirectoryAllInfoDto;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomAllInfoDto {

    private UUID classRoomId;
    private String classRoomName;
    private String description;
    private List<DirectoryAllInfoDto> directoryList;
    private List<String> teacherNames;
    private String code;
}
