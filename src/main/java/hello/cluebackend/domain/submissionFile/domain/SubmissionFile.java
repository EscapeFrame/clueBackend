package hello.cluebackend.domain.submissionFile.domain;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.submission.domain.Submission;
import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_content")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class SubmissionFile {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="submission_file_id")
  private Long assignmentContentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="user_id")
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="submission_id")
  private Submission assignmentCheck;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="assignment_id")
  private Assignment assignment;

  @Column(name="update_date")
  private LocalDateTime updateDate;
}