package hello.cluebackend.domain.submission.domain;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "submissions")
@Getter
@AllArgsConstructor
@Builder @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submission extends BaseEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "submission_id", nullable = false, updatable = false)
  private UUID submissionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignment_id")
  private Assignment assignment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(name = "is_submitted")
  private boolean isSubmitted;

  @Column(name = "submitted_at", nullable = true)
  private LocalDateTime submittedAt;

  public Submission(Assignment assignment, UserEntity user, boolean isSubmitted, LocalDateTime submittedAt) {
    if(assignment != null) { changeAssignment(assignment); }
    this.user = user;
    this.isSubmitted = isSubmitted;
    this.submittedAt = submittedAt;
  }

  public void changeAssignment(Assignment assignment) {
    this.assignment = assignment;
    assignment.getSubmissions().add(this);
  }

  // 과제 제출 취소
  public void cancel() {
    this.isSubmitted = false;
    this.submittedAt = LocalDateTime.now();
  }

  // 과제 제출
  public void submit() {
    this.isSubmitted = true;
    this.submittedAt = LocalDateTime.now();
  }

  // getter
  public boolean getIsSubmitted(){
    return this.isSubmitted;
  }
}
