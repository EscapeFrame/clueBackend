package hello.cluebackend.infrastructure.persistence.agent;

import hello.cluebackend.domain.agent.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgentJpaRepository extends JpaRepository<Agent, UUID> {

}
