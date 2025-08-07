package hello.cluebackend.assignment.domain;

import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Submission")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Submission {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "submission_id")
  private Long submissionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignment_id")
  private Assignment assignment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(name = "is_submitted")
  private Boolean isSubmitted;

  @Column(name = "submitted_at", nullable = true)
  private LocalDateTime submittedAt;
}
