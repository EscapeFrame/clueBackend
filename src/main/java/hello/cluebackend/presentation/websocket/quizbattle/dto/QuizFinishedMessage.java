package hello.cluebackend.presentation.websocket.quizbattle.dto;

import hello.cluebackend.domain.quizbattle.model.QuizRanking;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizFinishedMessage {
    private String roomCode;
    private List<QuizRanking> finalRankings;
    private String status;
    private String message;
}
