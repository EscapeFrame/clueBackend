package hello.cluebackend.domain.assignment.domain;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Assignment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "assignment_id")
  private Long assignmentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="class_id")
  private ClassRoom classRoom;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="creator_id")
  private User creator;

  @Column(name = "title")
  private String title;

  @Column(name="content")
  private String content;

  @Column(name="start_date")
  private LocalDateTime startDate;

  @Column(name="due_date")
  private LocalDateTime dueDate;
}