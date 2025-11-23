package hello.cluebackend.infrastructure.client.agent;

import hello.cluebackend.application.agent.dto.request.AgentRequest;
import hello.cluebackend.application.agent.dto.request.DocFeedbackRequest;
import hello.cluebackend.application.agent.dto.request.FlowFeedbackRequest;
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
  AgentResponse<AllAgentData> getAgent(@PathVariable UUID agent_id);

  @PostMapping("/api/v1/agents/{agent_id}/flow")
  AgentResponse<FlowData> createFlow(@PathVariable UUID agent_id);

  @PatchMapping("/api/v1/agents/{agent_id}/flow")
  AgentResponse<FlowData> updateFlow(@PathVariable UUID agent_id, @RequestBody FlowFeedbackRequest feedback);

  @PostMapping("/api/v1/agents/{agent_id}/doc")
  AgentResponse<DocData> createDoc(@PathVariable UUID agent_id);

  @PatchMapping("/api/v1/agents/{agent_id}/doc")
  AgentResponse<DocData> updateDoc(@PathVariable UUID agent_id, @RequestBody DocFeedbackRequest feedback);

  @PostMapping("/api/v1/agents/{agent_id}/complete")
  AgentResponse<CompleteData> complete(@PathVariable UUID agent_id);
}