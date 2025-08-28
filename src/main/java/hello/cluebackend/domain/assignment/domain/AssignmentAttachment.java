package hello.cluebackend.domain.assignment.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assignment_attachment")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssignmentAttachment extends BaseEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "assignment_attachment_id")
  private Long assignmentAttachmentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="assignment_id")
  private Assignment assignment;

  // FILE, URL
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private FileType type;

  // 실제 파일이면 S3 Key, URL이면 링크
  @Column(nullable = false)
  private String value;

  // 파일일 경우 메타데이터
  private String originalFileName;
  private String contentType;
  private Long size;
}
