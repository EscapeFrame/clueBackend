package hello.cluebackend.application.agent.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AgentDocResponse {
  @JsonProperty("agent_id")
  private String agentId;
  private String status;
  @JsonProperty("created_at")
  private ZonedDateTime createdAt;
}
