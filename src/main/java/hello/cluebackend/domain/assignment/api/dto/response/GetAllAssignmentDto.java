package hello.cluebackend.domain.assignment.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GetAllAssignmentDto {
  private Long assignmentId; // 과제 아이디
  private String title; // 과제 제목
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm") private LocalDateTime startDate; // 과제 시작일
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm") private LocalDateTime endDate; // 마감일

  public GetAllAssignmentDto(Long assignmentId, String title, LocalDateTime startDate, LocalDateTime endDate){
    this.assignmentId = assignmentId;
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}