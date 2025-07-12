package hello.cluebackend.domain.classroom.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Random;

@Getter
@Setter
@Builder
public class ClassRoomDTO {

    private Long classRoomId;
    private String name;
    private String description;
    @JsonIgnore
    private String code;
    private LocalDateTime createdAt;

    public void generateCode() {
        int length = 6;
        StringBuilder randomStringBuilder = new StringBuilder();
        Random random = new Random();
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            randomStringBuilder.append(characters.charAt(randomIndex));
        }
        this.code = randomStringBuilder.toString();
    }

    public ClassRoom toEntity() {
        return ClassRoom.builder()
                .name(name)
                .description(description)
                .code(code).build();
    }
}
