package hello.cluebackend.domain.quizbattle.model;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizRanking implements Serializable {

    private Integer rank;
    private UUID userId;
    private String username;
    private Integer totalScore;
    private Integer correctAnswers;
    private Integer totalQuestions;
    private Double accuracy;
    private Long averageResponseTime;

    public static QuizRanking fromParticipant(QuizParticipant participant, int totalQuestions) {
        int correct = participant.getCorrectAnswers() != null ? participant.getCorrectAnswers() : 0;
        double accuracy = totalQuestions > 0 ? (correct * 100.0 / totalQuestions) : 0.0;

        return QuizRanking.builder()
                .userId(participant.getUserId())
                .username(participant.getUsername())
                .totalScore(participant.getScore() != null ? participant.getScore() : 0)
                .correctAnswers(correct)
                .totalQuestions(totalQuestions)
                .accuracy(accuracy)
                .build();
    }
}
