package hello.cluebackend.application.agent.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class AgentFlowResponse {
  private String studyingName;
  private String learningPurpose;
  private List<String> mainWords;
  private List<String> links;
}