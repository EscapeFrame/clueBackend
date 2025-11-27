package hello.cluebackend.domain.submission.model;

import hello.cluebackend.domain.assignment.model.Assignment;
import hello.cluebackend.domain.assignment.exception.AccessDeniedException;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "submission")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Submission {
  @Id  @GeneratedValue(strategy = GenerationType.UUID)
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

  @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<SubmissionAttachment> submissionAttachments = new ArrayList<>();

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

  public void submissionValidator(UserEntity user){
    if(!this.user.getUserId().equals(user.getUserId())){
      throw new AccessDeniedException("삭제 권한이 없습니다.");
    }
  }
}