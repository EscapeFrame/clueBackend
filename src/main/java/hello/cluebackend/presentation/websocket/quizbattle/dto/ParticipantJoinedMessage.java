package hello.cluebackend.presentation.websocket.quizbattle.dto;

import hello.cluebackend.domain.quizbattle.model.QuizParticipant;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantJoinedMessage {
    private QuizParticipant participant;
    private Integer totalParticipants;
    private List<QuizParticipant> allParticipants;
    private String status;
    private String message;
}
