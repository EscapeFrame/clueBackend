package hello.cluebackend.assignment.management.domain;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Assignment")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "assignment_id")
  private Long assignmentId;

  @ManyToOne
  @JoinColumn(name = "classroom_id")
  private ClassRoom classRoom;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(name = "title")
  private String title;

  @Column(name = "content")
  private String content;

  @Column(name = "start_date")
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;
}