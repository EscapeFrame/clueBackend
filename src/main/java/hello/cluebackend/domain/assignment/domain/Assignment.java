package hello.cluebackend.domain.assignment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hello.cluebackend.domain.assignment.api.dto.request.ModifyAssignmentDto;
import hello.cluebackend.domain.submission.domain.Submission;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assignment")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Assignment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "assignment_id")
  private Long assignmentId;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "classroom_id")
  private ClassRoom classRoom;

  @JsonIgnore
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

  @JsonIgnore
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignment", cascade = CascadeType.REMOVE)
  private List<Submission> submissions = new ArrayList<>();

  public Assignment(ClassRoom classRoom,UserEntity user,String title, String content, LocalDateTime startDate, LocalDateTime endDate){
    this.classRoom = classRoom;
    this.user = user;
    this.title = title;
    this.content = content;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public void patch(ModifyAssignmentDto dto) {
    if (dto.getTitle() != null) this.title = dto.getTitle();
    if (dto.getContent() != null) this.content = dto.getContent();
    if (dto.getStartDate() != null) this.startDate = dto.getStartDate();
    if (dto.getEndDate() != null) this.endDate = dto.getEndDate();
  }
}