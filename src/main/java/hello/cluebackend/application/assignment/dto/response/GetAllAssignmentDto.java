package hello.cluebackend.application.assignment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import hello.cluebackend.domain.assignment.model.Assignment;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetAllAssignmentDto(
        UUID assignmentId, // 과제 아이디
        String title,      // 과제 제목
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime startDate, // 과제 시작일
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime endDate    // 마감일
) {
  public static GetAllAssignmentDto from(Assignment assignment){
    return new GetAllAssignmentDto (
      assignment.getAssignmentId(),
      assignment.getTitle(),
      assignment.getStartDate(),
      assignment.getEndDate()
    );
  }
}