package hello.cluebackend.application.agent.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AllAgentData {
    @JsonProperty("agent_id")
    private UUID agentId;
    private String status;
    private Planning planning;
    private FlowData flow;
    private DocData doc;
    private GraphData graph;
}
