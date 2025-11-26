package hello.cluebackend.domain.quizbattle.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizQuestion implements Serializable {

    private Integer questionNumber;
    private String questionText;
    private List<QuizOption> options;
    private Integer correctAnswer;
    private Integer timeLimit;
    private String explanation;
    private String difficulty;

    public boolean isCorrect(Integer answerIndex) {
        return correctAnswer != null && correctAnswer.equals(answerIndex);
    }
}
