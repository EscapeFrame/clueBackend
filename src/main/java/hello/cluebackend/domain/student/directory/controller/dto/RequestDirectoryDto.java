package hello.cluebackend.domain.student.directory.controller.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RequestDirectoryDto {
    private UUID directoryId;
    private UUID classRoomId;
    private String name;
    private int directoryOrder;
}
