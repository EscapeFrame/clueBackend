package hello.cluebackend.domain.assignment.domain;

import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "assignment")
@Getter
@NoArgsConstructor
public class AssignmentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="assignment")
  private Long assignmentId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="class_room_id", updatable = false)
  private classRoom classRoomId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="user_id", updatable = false)
  private UserEntity userId;

  @Column(name = "title")
  private String title;

  @Column(name = "content")
  private String content;

  @Column(name = "start_date")
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;

  @Builder
  public AssignmentEntity(String title, String content, String startDate, String endDate){
    this.title = title;
    this. content = content;
    this.startDate = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
    this.endDate = LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
  }


}