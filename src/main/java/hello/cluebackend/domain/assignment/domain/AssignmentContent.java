package hello.cluebackend.domain.assignment.domain;

import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_content")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentContent {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="assignment_content_id")
  private Long assignmentContentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="user_id")
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="assignment_id")
  private Assignment assignment;

  @JoinColumn(name="original_file_name")
  private String originalFileName;

  @Column(name="stored_file_name")
  private String storedFileName;

  @Column(name="file_path")
  private String filePath;

  @Column(name="file_size")
  private int fileSize;

  @Enumerated(EnumType.STRING)
  @Column(name = "submit_type")
  private SubmitType submitType; // 1. 랑크 , 2. 파일

  @Column(name="update_date")
  private LocalDateTime updateDate;
}
