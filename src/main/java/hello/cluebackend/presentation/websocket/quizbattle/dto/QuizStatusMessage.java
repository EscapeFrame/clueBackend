package hello.cluebackend.presentation.websocket.quizbattle.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizStatusMessage {
    private String status;
    private String message;
}
