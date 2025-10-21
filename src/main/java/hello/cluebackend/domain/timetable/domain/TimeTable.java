package hello.cluebackend.domain.timetable.domain;

import com.nimbusds.openid.connect.sdk.SubjectType;
import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "timetable")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TimeTable {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long timetable_id;

  private SubjectType subjectName;
  private String dayOfWeek;
  private String period;

  @Enumerated(EnumType.STRING)
  private SubjectType subject;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;
}
