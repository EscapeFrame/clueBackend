package hello.cluebackend.presentation.api.agent;

import hello.cluebackend.application.agent.dto.request.AgentRequest;
import hello.cluebackend.application.agent.dto.request.DocFeedbackRequest;
import hello.cluebackend.application.agent.dto.request.FlowFeedbackRequest;
import hello.cluebackend.application.agent.dto.response.*;
import hello.cluebackend.application.user.dto.oauth2.CustomOAuth2User;
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

  // 1단계: 에이전트 생성 (Planning 단계)
  @PostMapping
  public ResponseEntity<AgentResponse<AgentData>> createAgent(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @RequestBody AgentRequest agentRequest
  ) {
    AgentResponse<AgentData> agentResponse = agentClient.createAgent(agentRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(agentResponse);
  }

  // Agent 전체 상태 조회
  @GetMapping("/{agentId}")
  public ResponseEntity<AgentResponse<AllAgentData>> getAgent(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID agentId
  ) {
    AgentResponse<AllAgentData> getAgent = agentClient.getAgent(agentId);
    return ResponseEntity.status(HttpStatus.OK).body(getAgent);
  }

  // 2단계: Flow 생성 (목차 생성)
  @PostMapping("/{agent_id}/flow")
  public ResponseEntity<AgentResponse<FlowData>> createFlow(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID agent_id
  ) {
    AgentResponse<FlowData> flowResponse = agentClient.createFlow(agent_id);
    return ResponseEntity.status(HttpStatus.CREATED).body(flowResponse);
  }

  // 3단계: Flow 피드백
  @PatchMapping("/{agent_id}/flow")
  public ResponseEntity<AgentResponse<FlowData>> updateFlow(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID agent_id,
          @RequestBody FlowFeedbackRequest feedback
  ) {
    AgentResponse<FlowData> flowResponse = agentClient.updateFlow(agent_id, feedback);
    return ResponseEntity.status(HttpStatus.OK).body(flowResponse);
  }

  // 4단계: Doc 생성 (본문 생성)
  @PostMapping("/{agent_id}/doc")
  public ResponseEntity<AgentResponse<DocData>> createDoc(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID agent_id
  ) {
    AgentResponse<DocData> docResponse = agentClient.createDoc(agent_id);
    return ResponseEntity.status(HttpStatus.CREATED).body(docResponse);
  }

  // 5단계: Doc 피드백
  @PatchMapping("/{agent_id}/doc")
  public ResponseEntity<AgentResponse<DocData>> updateDoc(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID agent_id,
          @RequestBody DocFeedbackRequest feedback
  ) {
    AgentResponse<DocData> docResponse = agentClient.updateDoc(agent_id, feedback);
    return ResponseEntity.status(HttpStatus.OK).body(docResponse);
  }

  @PostMapping("/{agent_id}/complete")
  public ResponseEntity<AgentResponse<CompleteData>> complete(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID agent_id
  ) {
    AgentResponse<CompleteData> completeResponse = agentClient.complete(agent_id);
    return ResponseEntity.status(HttpStatus.OK).body(completeResponse);
  }
}
