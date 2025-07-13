package hello.cluebackend.domain.classroom.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ClassRoomDto {
    private Long classRoomId;
    private String name;
    private String description;
    private String sort;
    private String target;
    @JsonIgnore
    private String code;
    private Boolean isActivation;
    private LocalDateTime createdAt;

    public void generateCode() {
        int length = 6;
        StringBuilder randomStringBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            randomStringBuilder.append(characters.charAt(randomIndex));
        }
        this.code = randomStringBuilder.toString();
    }

    public ClassRoom toEntity() {
        return ClassRoom.builder()
                .classRoomId(classRoomId)
                .name(name)
                .description(description)
                .sort(sort)
                .target(target)
                .isActivation(isActivation)
                .code(code)
                .createdAt(createdAt)
                .build();
    }
}
