package hello.cluebackend.domain.subject.domain;

import hello.cluebackend.domain.subject.presentation.dto.request.SubjectRequest;
import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subject")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Subject {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long subjectId;

  @Column(nullable = false, length = 100)
  private String subjectName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SubjectCategory subjectCategory;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SubjectType subjectType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity teacher;

  @Column(nullable = false)
  private int weeklyHours;

  @Column(nullable = false)
  private int grade;

  public void updateDetails(SubjectRequest request) {
    this.subjectName = request.subjectName();
    this.subjectType = request.subjectType();
    this.subjectCategory = request.subjectCategory();
    this.weeklyHours = request.weeklyHours();
    this.grade = request.grade();
  }
}
