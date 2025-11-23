package hello.cluebackend.presentation.websocket.quizbattle.dto;

import hello.cluebackend.domain.quizbattle.model.QuizParticipant;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantLeftMessage {
    private UUID userId;
    private Integer totalParticipants;
    private List<QuizParticipant> allParticipants;
    private String status;
}
