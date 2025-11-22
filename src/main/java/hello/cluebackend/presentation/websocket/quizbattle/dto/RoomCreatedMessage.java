package hello.cluebackend.presentation.websocket.quizbattle.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreatedMessage {
    private String roomCode;
    private String title;
    private UUID hostId;
    private Integer maxParticipants;
    private Integer questionCount;
    private Integer timePerQuestion;
    private String status;
    private String message;
}
