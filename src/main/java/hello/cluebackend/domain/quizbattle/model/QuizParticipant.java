package hello.cluebackend.domain.quizbattle.model;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizParticipant implements Serializable {

    private UUID userId;
    private String username;
    private String sessionId;
    private Integer score;
    private Integer correctAnswers;
    private Boolean isReady;
    private Long joinedAt;

    public void addScore(int points) {
        this.score = (this.score != null ? this.score : 0) + points;
    }

    public void incrementCorrectAnswers() {
        this.correctAnswers = (this.correctAnswers != null ? this.correctAnswers : 0) + 1;
    }

    public void reset() {
        this.score = 0;
        this.correctAnswers = 0;
        this.isReady = false;
    }
}
