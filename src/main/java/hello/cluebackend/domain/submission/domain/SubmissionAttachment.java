package hello.cluebackend.domain.submission.domain;

import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "submission_attachment")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubmissionAttachment {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name="submission_attachment_id", nullable = false, updatable = false)
  private UUID SubmissionAttachmentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="user_id")
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="submission_id")
  private Submission submission;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private FileType type;

  @Column(nullable = false)
  private String value;

  private String originalFileName;
  private String contentType;
  private Long size;
}