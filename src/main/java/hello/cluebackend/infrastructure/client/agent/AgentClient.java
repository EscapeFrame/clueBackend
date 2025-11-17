package hello.cluebackend.infrastructure.client.agent;

import hello.cluebackend.application.agent.dto.request.AgentRequest;
import hello.cluebackend.application.agent.dto.response.*;
import hello.cluebackend.config.FeignLoggerConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "agentClient", url = "${fastapi.url}", configuration = FeignLoggerConfig.class)
public interface AgentClient {
  @PostMapping("/api/v1/agents")
  AgentResponse<AgentData> createAgent(@RequestBody AgentRequest agentRequest);

  @GetMapping("/api/v1/agents/{agent_id}")
  AgentResponse<AgentAllDataResponse> getAgent(@PathVariable UUID agent_id);

  @PostMapping("/api/v1/agents/{agent_id}/flow")
  AgentResponse<AgentFlowResponse> createFlow(@PathVariable UUID agent_id);

  @PostMapping("/api/v1/agents/{agent_id}/doc")
  AgentResponse<AgentDocResponse> createDoc(@PathVariable UUID agent_id);

  @PostMapping("/api/v1/agents/{agent_id}/graph")
  AgentResponse<AgentGraphResponse> createGraph(@PathVariable UUID agent_id);
}