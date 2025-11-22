package hello.cluebackend.presentation.websocket.quizbattle.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private String status;
    private String message;
}
