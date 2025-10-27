package hello.cluebackend.domain.subject.model;

import hello.cluebackend.domain.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

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

  public void updateDetails(String subjectName, SubjectCategory subjectCategory, SubjectType subjectType, int weeklyHours, int grade) {
    this.subjectName = subjectName;
    this.subjectType = subjectType;
    this.subjectCategory = subjectCategory;
    this.weeklyHours = weeklyHours;
    this.grade = grade;
  }
}
