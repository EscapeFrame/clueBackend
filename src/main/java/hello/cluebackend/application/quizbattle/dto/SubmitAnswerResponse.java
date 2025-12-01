package hello.cluebackend.application.quizbattle.dto;

import hello.cluebackend.domain.quizbattle.model.QuizAnswer;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubmitAnswerResponse {
    private final QuizAnswer answer;
    private final int totalAnswers;
}
