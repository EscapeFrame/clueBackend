package hello.cluebackend.domain.assignment.domain;

import hello.cluebackend.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "assignment_content")
@Getter @Setter
public class AssignmentContent {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="assignment_content_id")
  private Long assignmentContentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @Column(name="user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @Column(name="assignment_check_id")
  private AssignmentCheck assignmentCheck;

  @ManyToOne(fetch = FetchType.LAZY)
  @Column(name="assignment_id")
  private Assignment assignment;

  @Column(name="file_name")
  private String fileName;

  @Column(name="submit_type")
  private int submitType;

  @Column(name="assignment_link")
  private String assignmentLink;
}
