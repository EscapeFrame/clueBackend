package hello.cluebackend.presentation.websocket.quizbattle.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomCancelledMessage {
    private String roomCode;
    private String reason;
    private String status;
    private String message;
}
