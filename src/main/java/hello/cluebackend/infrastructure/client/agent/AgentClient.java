package hello.cluebackend.infrastructure.client.agent;

import hello.cluebackend.application.agent.dto.request.AgentRequest;
import hello.cluebackend.application.agent.dto.response.AgentDocResponse;
import hello.cluebackend.application.agent.dto.response.AgentFlowResponse;
import hello.cluebackend.application.agent.dto.response.AgentGraphResponse;
import hello.cluebackend.application.agent.dto.response.AgentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "agentClient", url = "${fastapi.url}")
public interface AgentClient {
  @PostMapping("/api/v1/agents")
  AgentResponse createAgent(@RequestBody AgentRequest agentRequest);

  @PostMapping("/api/v1/agents/{agent_id}/flow")
  AgentFlowResponse createFlow(@PathVariable UUID agent_id);

  @PatchMapping("/api/v1/agents/{agent_id}/flow")
  AgentFlowResponse patchFlow(@PathVariable UUID agent_id );

  @PostMapping("/api/v1/agents/{agent_id}/doc")
  AgentDocResponse createDoc(@PathVariable UUID agent_id);

  @PatchMapping("/api/v1/agents/{agent_id}/doc")
  AgentDocResponse patchDoc(@PathVariable UUID agent_id);

  @PostMapping("/api/v1/agents/{agent_id}/graph")
  AgentGraphResponse createGraph(@PathVariable UUID agent_id);
}
