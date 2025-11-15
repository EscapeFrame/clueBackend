package hello.cluebackend.presentation.api.agent;

import hello.cluebackend.application.agent.dto.request.AgentRequest;
import hello.cluebackend.application.agent.dto.response.AgentDocResponse;
import hello.cluebackend.application.agent.dto.response.AgentFlowResponse;
import hello.cluebackend.application.agent.dto.response.AgentGraphResponse;
import hello.cluebackend.application.agent.dto.response.AgentResponse;
import hello.cluebackend.application.user.dto.CustomOAuth2User;
import hello.cluebackend.infrastructure.client.agent.AgentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agents")
@RequiredArgsConstructor
@Slf4j
public class AgentController {
  private final AgentClient agentClient;

  // 에이전트 생성
  @PostMapping
  public ResponseEntity<AgentResponse> createAgent(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @RequestBody AgentRequest agentRequest
  ) {
    AgentResponse agentResponse = agentClient.createAgent(agentRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(agentResponse);
  }

  // 플로우 생성
  @PostMapping("/{agent_id}/flow")
  public ResponseEntity<AgentFlowResponse> createFlow(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID agent_id
  ) {
    AgentFlowResponse agentFlowResponse = agentClient.createFlow(agent_id);
    return ResponseEntity.status(HttpStatus.CREATED).body(agentFlowResponse);
  }

  @PatchMapping("/{agent_id}/flow")
  public ResponseEntity<AgentFlowResponse> patchFlow(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID agent_id
  ) {
    AgentFlowResponse agentFlowResponse = agentClient.patchFlow(agent_id);
    return ResponseEntity.status(HttpStatus.CREATED).body(agentFlowResponse);
  }

  @PostMapping("/{agent_id}/doc")
  public ResponseEntity<AgentDocResponse> createDoc(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID agent_id
  ) {
    AgentDocResponse agentDocResponse = agentClient.createDoc(agent_id);
    return ResponseEntity.status(HttpStatus.CREATED).body(agentDocResponse);
  }

  @PatchMapping("/{agent_id}/doc")
  public ResponseEntity<AgentDocResponse> patchDoc(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID agent_id
  ) {
    AgentDocResponse agentDocResponse = agentClient.patchDoc(agent_id);
    return ResponseEntity.status(HttpStatus.CREATED).body(agentDocResponse);
  }

  @PostMapping("/{agent_id}/graph")
  public ResponseEntity<AgentGraphResponse> createGraph(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID agent_id
  ) {
    AgentGraphResponse agentGraphResponse = agentClient.createGraph(agent_id);
    return ResponseEntity.status(HttpStatus.CREATED).body(agentGraphResponse);
  }
}
