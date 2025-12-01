package hello.cluebackend.application.directory.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RequestDirectoryDto {
    @Nullable
    private UUID directoryId;

    @NotNull(message="클래스룸 ID는 필수입니다.")
    private UUID classRoomId;

    @NotBlank(message = "디렉토리 이름은 필수입니다.")
    private String name;
}
