package hello.cluebackend.application.agent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GraphNode {
    private int id;
    private String keyword;
    private List<Integer> links;
}
