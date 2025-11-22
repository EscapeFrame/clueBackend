package hello.cluebackend.domain.quizbattle.model;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizAnswer implements Serializable {

    private UUID userId;
    private String roomCode;
    private Integer questionNumber;
    private Integer answerIndex;
    private Long submittedAt;
    private Integer timeSpent;
    private Boolean isCorrect;
    private Integer points;

    public void calculatePoints(boolean correct, int basePoints, int timeBonus) {
        this.isCorrect = correct;
        if (correct) {
            this.points = basePoints + timeBonus;
        } else {
            this.points = 0;
        }
    }
}
