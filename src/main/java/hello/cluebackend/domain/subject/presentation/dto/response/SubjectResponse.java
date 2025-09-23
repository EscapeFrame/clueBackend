package hello.cluebackend.domain.subject.presentation.dto.response;

import hello.cluebackend.domain.subject.domain.Subject;
import hello.cluebackend.domain.subject.domain.SubjectCategory;
import hello.cluebackend.domain.subject.domain.SubjectType;

public record SubjectResponse(
        String subjectName,
        SubjectCategory subjectCategory,
        SubjectType subjectType,
        String teacherId,
        int weeklyHours,
        int grade
) {
  public static SubjectResponse from(Subject subject) {
    return new SubjectResponse(
            subject.getSubjectName(),
            subject.getSubjectCategory(),
            subject.getSubjectType(),
            subject.getTeacher().getUsername(),
            subject.getWeeklyHours(),
            subject.getGrade()
    );
  }
}