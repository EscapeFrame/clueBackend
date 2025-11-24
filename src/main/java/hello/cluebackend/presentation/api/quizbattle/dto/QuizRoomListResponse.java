package hello.cluebackend.presentation.api.quizbattle.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizRoomListResponse {
    private String roomCode;
    private String hostName;
    private String status;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private Integer questionCount;
    private Integer timePerQuestion;
    private LocalDateTime createdAt;
}
