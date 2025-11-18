package hello.cluebackend.application.agent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AgentFlowResponse {
    private UUID agentId;
    private String status;
    private Flow flow;
}
