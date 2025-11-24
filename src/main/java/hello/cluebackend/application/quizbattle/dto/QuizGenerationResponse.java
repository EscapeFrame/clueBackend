package hello.cluebackend.application.quizbattle.dto;

import hello.cluebackend.domain.quizbattle.model.QuizQuestion;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizGenerationResponse {

    private List<QuizQuestion> questions;
    private Integer totalQuestions;
    private String status;
}
