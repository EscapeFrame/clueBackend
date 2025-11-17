package hello.cluebackend.application.agent.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AgentData {
    @JsonProperty("agent_id")
    private UUID agentId;
    private String status;
    @JsonProperty("created_at")
    private ZonedDateTime createdAt;
}
