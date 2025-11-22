package hello.cluebackend.presentation.websocket.quizbattle.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizQuestionMessage {
    private Integer questionNumber;
    private String questionText;
    private List<String> options;
    private Integer timeLimit;
    private String difficulty;
    private String status;
}
