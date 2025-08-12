package hello.cluebackend.assignment.participation.domain.SubmissionAttachment;

import hello.cluebackend.assignment.management.domain.Assignment;
import hello.cluebackend.assignment.participation.domain.Submission;
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
public abstract class SubmissionAttachment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="submission_attachment_id")
  private Long SubmissionAttachmentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="user_id")
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="submission_id")
  private Submission submission;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="assignment_id")
  private Assignment assignment;

  @Column(name="update_date")
  private LocalDateTime updateDate;
}