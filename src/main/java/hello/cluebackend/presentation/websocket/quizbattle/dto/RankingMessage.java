package hello.cluebackend.presentation.websocket.quizbattle.dto;

import hello.cluebackend.domain.quizbattle.model.QuizRanking;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingMessage {
    private List<QuizRanking> rankings;
    private Integer totalParticipants;
    private String status;
}
