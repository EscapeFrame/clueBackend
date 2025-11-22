package hello.cluebackend.application.quizbattle.dto;

import lombok.*;

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
}
