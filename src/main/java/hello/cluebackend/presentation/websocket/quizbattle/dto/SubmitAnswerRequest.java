package hello.cluebackend.presentation.websocket.quizbattle.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitAnswerRequest {
    private Integer questionNumber;
    private Integer answerIndex;
    private Long submittedAt;
    private Integer timeSpent;
}
