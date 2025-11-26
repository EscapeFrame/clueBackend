package hello.cluebackend.application.quizbattle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizGenerationRequest {
    private Integer questionCount;
    private String difficulty;
    private String language;
    @JsonProperty("document_id")
    private UUID documentId;  // RAG 기반 문제 생성을 위한 문서 ID
}