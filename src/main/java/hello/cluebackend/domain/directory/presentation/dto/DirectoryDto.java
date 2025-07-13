package hello.cluebackend.domain.directory.presentation.dto;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.directory.domain.Directory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectoryDto {

    private Long directoryId;
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
