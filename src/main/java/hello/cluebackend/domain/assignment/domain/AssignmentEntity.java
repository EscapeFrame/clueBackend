package hello.cluebackend.domain.assignment.domain;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "assignment")
@Getter
@NoArgsConstructor
public class AssignmentEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="assignment")
  private Long assignmentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="class_room_id", updatable = false)
  private ClassRoom classRoom;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="user_id", updatable = false)
  private UserEntity user;

  @Column(name = "title")
  private String title;

  @Column(name = "content")
  private String content;

  @Column(name = "start_date")
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;

  @Builder
  public AssignmentEntity(String title, String content, String startDate, String endDate, ClassRoom classRoom, UserEntity user){
    this.title = title;
    this. content = content;
    this.startDate = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    this.endDate = LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    this.classRoom = classRoom;
    this.user = user;
  }
}