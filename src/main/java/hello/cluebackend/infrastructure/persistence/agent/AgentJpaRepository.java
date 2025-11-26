package hello.cluebackend.infrastructure.persistence.agent;

import hello.cluebackend.domain.agent.model.Agent;
import hello.cluebackend.domain.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgentJpaRepository extends JpaRepository<Agent, UUID> {

    void deleteByUser(UserEntity user);
}
