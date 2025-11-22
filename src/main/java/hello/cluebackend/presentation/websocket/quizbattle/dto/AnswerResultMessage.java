package hello.cluebackend.presentation.websocket.quizbattle.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerResultMessage {
    private Integer questionNumber;
    private Boolean isCorrect;
    private Integer points;
    private String status;
}
