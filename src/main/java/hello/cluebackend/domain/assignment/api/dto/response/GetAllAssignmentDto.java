package hello.cluebackend.domain.assignment.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class GetAllAssignmentDto {
  private UUID assignmentId; // 과제 아이디
  private String title; // 과제 제목
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm") private LocalDateTime startDate; // 과제 시작일
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm") private LocalDateTime endDate; // 마감일

  public GetAllAssignmentDto(UUID assignmentId, String title, LocalDateTime startDate, LocalDateTime endDate){
    this.assignmentId = assignmentId;
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}