package hello.cluebackend.application.quizbattle.dto;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizGenerationRequest {

    private String topic;
    private Integer questionCount;
    private String difficulty;
    private String language;
    private String context;
    private UUID documentId;  // RAG 기반 문제 생성을 위한 문서 ID
}
