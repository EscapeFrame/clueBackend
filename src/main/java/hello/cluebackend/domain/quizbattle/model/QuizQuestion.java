package hello.cluebackend.domain.quizbattle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizQuestion implements Serializable {

    @JsonProperty("question_number")
    private Integer questionNumber;

    @JsonProperty("question_text")
    private String questionText;

    @JsonProperty("options")
    private List<QuizOption> options;

    @JsonProperty("correct_answer")
    private Integer correctAnswer;

    @JsonProperty("time_limit")
    private Integer timeLimit;

    @JsonProperty("explanation")
    private String explanation;

    @JsonProperty("difficulty")
    private String difficulty;

    public boolean isCorrect(Integer answerIndex) {
        return correctAnswer != null && correctAnswer.equals(answerIndex);
    }
}
