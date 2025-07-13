package hello.cluebackend.domain.directory.presentation.dto;

import lombok.Getter;

@Getter
public class CreateDirectoryDto {
    private Long classRoomId;
    private String name;
    private int directoryOrder;
}
