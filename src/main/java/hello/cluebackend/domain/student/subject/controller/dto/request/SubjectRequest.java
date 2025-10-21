package hello.cluebackend.domain.student.subject.controller.dto.request;

import hello.cluebackend.domain.student.subject.domain.Subject;
import hello.cluebackend.domain.student.subject.domain.SubjectCategory;
import hello.cluebackend.domain.student.subject.domain.SubjectType;
import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubjectRequest(
        @NotBlank(message = "과목 이름은 필수입니다.")
        String subjectName,

        @NotNull(message = "과목 카테고리는 필수입니다.")
        SubjectCategory subjectCategory,

        @NotNull(message = "과목 타입은 필수입니다.")
        SubjectType subjectType,

        @Min(value = 1, message = "주간 수업 시간은 1 이상이어야 합니다.")
        int weeklyHours,

        @NotBlank(message = "학년은 필수입니다.")
        int grade
) {
  public static Subject toEntity(SubjectRequest request, UserEntity teacher) {
    return Subject.builder()
            .subjectName(request.subjectName())
            .subjectCategory(request.subjectCategory())
            .subjectType(request.subjectType())
            .weeklyHours(request.weeklyHours())
            .grade(request.grade())
            .teacher(teacher)
            .build();
  }
}
