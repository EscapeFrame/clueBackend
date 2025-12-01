package hello.cluebackend.application.quizbattle.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class RevealAnswerResponse {
    private final int correctAnswer;
    private final String explanation;
    private final Map<Integer, Integer> statistics;
    private final int totalAnswers;
}