package hello.cluebackend.domain.assignment.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import hello.cluebackend.domain.submission.domain.Submission;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "assignments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Assignment extends BaseEntity {
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

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDate;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
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

  // patch 메서드 (null 체크 후 업데이트)
  public void patch(String title, String content, LocalDateTime startDate, LocalDateTime endDate) {
    if (title != null) this.title = title;
    if (content != null) this.content = content;
    if (startDate != null) this.startDate = startDate;
    if (endDate != null) this.endDate = endDate;
  }
}