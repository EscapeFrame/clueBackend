package hello.cluebackend.domain.assignment.domain;

import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="assignment_check")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentCheck {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long assignmentCheckId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignment_id")
  private Assignment assignment;

  @Column(name="is_submitted")
  private Boolean isSubmitted;

  @Column(name="submitted_at")
  private LocalDateTime submittedAt;
}
