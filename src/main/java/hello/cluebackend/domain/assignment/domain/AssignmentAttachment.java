package hello.cluebackend.domain.assignment.domain;

import hello.cluebackend.domain.assignment.presentation.dto.AssignmentAttachmentDto;
import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_attachment")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentAttachment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long assignmentAttachmentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="assignment_id")
  private Assignment assignment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(name = "original_file_name")
  private String originalFileName;

  @Column(name = "stored_file_name")
  private String storedFileName;

  @Column(name = "file_path")
  private String filePath;

  @Column(name = "file_size")
  private Integer fileSize;

  @Enumerated(EnumType.STRING)
  @Column(name = "submit_type")
  private SubmitType submitType;

  @Column(name = "update_date")
  private LocalDateTime updateDate;

  public AssignmentAttachmentDto toDto() {
    return AssignmentAttachmentDto.builder()
            .assignmentAttachmentId(assignmentAttachmentId)
            .assignment(assignment)
            .user(user)
            .originalFileName(originalFileName)
            .storedFileName(storedFileName)
            .filePath(filePath)
            .fileSize(fileSize)
            .submitType(submitType)
            .updateDate(updateDate)
            .build();
  }
}
