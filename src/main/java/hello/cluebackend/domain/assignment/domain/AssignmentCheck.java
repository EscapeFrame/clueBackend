package hello.cluebackend.domain.assignment.domain;

import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_check")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentCheck {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="assignment_check_id")
  private Long assignmentCheckId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignment_id")
  private Assignment assignment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(name = "is_submitted")
  private Boolean isSubmitted;

  @Column(name = "subbmited_at")
  private LocalDateTime submittedAt;
}
