package hello.cluebackend.presentation.websocket.quizbattle.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomRequest {
    private String title;
    private String topic;
    private Integer maxParticipants;
    private Integer questionCount;
    private Integer timePerQuestion;
    private UUID classRoomId;
    private UUID documentId;  // RAG 기반 문제 생성을 위한 문서 ID
}
