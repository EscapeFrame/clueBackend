package hello.cluebackend.domain.assignment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import hello.cluebackend.domain.submission.model.Submission;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "assignment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Assignment {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "assignment_id", nullable = false, updatable = false)
  private UUID assignmentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "classroom_id")
  private ClassRoom classRoom;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String content;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  @OneToMany(mappedBy = "assignment", cascade = CascadeType.REMOVE, orphanRemoval = true)
  @Builder.Default
  @JsonIgnore
  private List<Submission> submissions = new ArrayList<>();

  // DTO 기반 정적 팩토리 메서드
  public static Assignment create(ClassRoom classRoom, UserEntity user, String title, String content, LocalDateTime startDate, LocalDateTime endDate) {
    return Assignment.builder()
            .classRoom(classRoom)
            .user(user)
            .title(title)
            .content(content)
            .startDate(startDate)
            .endDate(endDate)
            .build();
  }
  public static Assignment of(ClassRoom classRoom, UserEntity user, String title, String content, LocalDateTime startDate, LocalDateTime endDate) {
    return Assignment.builder()
            .classRoom(classRoom)
            .user(user)
            .title(title)
            .content(content)
            .startDate(startDate)
            .endDate(endDate)
            .build();
  }


  public void updateDetails(String title, String content, LocalDateTime startDate, LocalDateTime endDate) {
    this.title = title;
    this.content = content;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}