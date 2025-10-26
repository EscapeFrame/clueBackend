package hello.cluebackend.application.directory.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RequestDirectoryDto {
    private UUID directoryId;
    private UUID classRoomId;
    private String name;
    private int directoryOrder;
}
