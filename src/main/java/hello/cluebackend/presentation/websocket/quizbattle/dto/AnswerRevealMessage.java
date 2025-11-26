package hello.cluebackend.presentation.websocket.quizbattle.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerRevealMessage {
    private Integer questionNumber;
    private Integer correctAnswer;
    private String explanation;
    private Map<Integer, Integer> statistics;
    private Integer totalAnswers;
    private String status;
    private String message;
}
