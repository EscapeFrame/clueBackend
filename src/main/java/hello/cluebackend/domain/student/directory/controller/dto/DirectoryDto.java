package hello.cluebackend.domain.student.directory.controller.dto;

import hello.cluebackend.domain.student.classroom.domain.ClassRoom;
import hello.cluebackend.domain.student.directory.domain.Directory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DirectoryDto {

    private UUID directoryId;
    private ClassRoom classRoom;
    private String name;
    private int directoryOrder;

    public Directory toDto() {
        return Directory.builder()
                .directoryId(directoryId)
                .classRoom(classRoom)
                .name(name)
                .directoryOrder(directoryOrder)
                .build();
    }
}
