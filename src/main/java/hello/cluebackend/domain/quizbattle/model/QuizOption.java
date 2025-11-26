package hello.cluebackend.domain.quizbattle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizOption implements Serializable {
    @JsonProperty("index")
    private Integer index;

    @JsonProperty("text")
    private String text;
}
