package hello.cluebackend.application.agent.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WordItem {
    private int priority;
    private String index;
    private int iconNumber;
}
