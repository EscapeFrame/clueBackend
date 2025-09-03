package hello.cluebackend.domain.classroom.presentation.dto;

import hello.cluebackend.domain.directory.presentation.dto.DirectoryAllInfoDto;
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
}
