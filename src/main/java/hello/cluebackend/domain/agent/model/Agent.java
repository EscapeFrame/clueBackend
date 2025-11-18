package hello.cluebackend.domain.agent.model;

import hello.cluebackend.domain.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "agents")
@Getter @ToString(exclude = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agent {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private UUID agentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  public static Agent create(UserEntity userEntity) {
    Agent agent = new Agent();
    agent.user = userEntity;
    return agent;
  }
}