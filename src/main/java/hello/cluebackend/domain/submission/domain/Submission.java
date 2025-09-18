package hello.cluebackend.domain.submission.domain;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "submission")
@Getter
@AllArgsConstructor
@Builder @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submission {
  @Id  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "submission_id", nullable = false, updatable = false)
  private UUID submissionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignment_id")
  private Assignment assignment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "class_room_id")
  private ClassRoom classRoom;

  @Column(name = "is_submitted")
  private boolean isSubmitted;

  @Column(name = "submitted_at", nullable = true)
  private LocalDateTime submittedAt;

  public Submission(Assignment assignment, UserEntity user, ClassRoom classRoom ,boolean isSubmitted, LocalDateTime submittedAt) {
    this.assignment = assignment;
    this.user = user;
    this.classRoom = classRoom;
    this.isSubmitted = isSubmitted;
    this.submittedAt = submittedAt;
  }

  // 과제 제출 취소
  public void cancel() {
    this.isSubmitted = false;
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
