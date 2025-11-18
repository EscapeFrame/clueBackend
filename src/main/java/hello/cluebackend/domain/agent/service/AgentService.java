package hello.cluebackend.domain.agent.service;

import hello.cluebackend.domain.agent.model.Agent;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.infrastructure.persistence.agent.AgentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgentService {
  private final AgentJpaRepository agentRepository;

  public void createAgent(UserEntity userEntity) {
    Agent agent = Agent.create(userEntity);
  }
}
