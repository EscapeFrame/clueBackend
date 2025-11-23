package hello.cluebackend.application.agent.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AgentAllDataResponse {
  private Boolean success;
  private String message;
  private AllAgentData data;
  private String error;
}