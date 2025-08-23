package hello.cluebackend.domain.assignment.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GetAllClassRoomAssignmentDto {
  private Long assignmentId;
  private String title;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm") private LocalDateTime startDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm") private LocalDateTime endDate;

  public GetAllClassRoomAssignmentDto(Long assignmentId, String title, LocalDateTime startDate, LocalDateTime endDate){
    this.assignmentId = assignmentId;
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}
