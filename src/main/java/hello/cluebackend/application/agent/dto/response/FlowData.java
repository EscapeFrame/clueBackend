package hello.cluebackend.application.agent.dto.response;

import hello.cluebackend.application.agent.dto.request.WordItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FlowData {
    private List<WordItem> words;
}
