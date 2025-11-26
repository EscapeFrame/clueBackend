package hello.cluebackend.domain.quizbattle.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizOption implements Serializable {
    private Integer index;
    private String text;
}
