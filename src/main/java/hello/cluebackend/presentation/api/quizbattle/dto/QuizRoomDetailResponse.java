package hello.cluebackend.presentation.api.quizbattle.dto;

import hello.cluebackend.domain.quizbattle.model.QuizParticipant;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizRoomDetailResponse {
    private String roomCode;
    private String title;
    private String topic;
    private UUID hostId;
    private String hostName;
    private String status;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private Integer questionCount;
    private Integer timePerQuestion;
    private List<QuizParticipant> participants;
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
}
