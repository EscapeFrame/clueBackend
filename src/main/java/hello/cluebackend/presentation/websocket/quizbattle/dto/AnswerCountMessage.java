package hello.cluebackend.presentation.websocket.quizbattle.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerCountMessage {
    private String status;
    private String message;
    private int questionNumber;
    private int totalAnswers;
    private int totalParticipants;
}
