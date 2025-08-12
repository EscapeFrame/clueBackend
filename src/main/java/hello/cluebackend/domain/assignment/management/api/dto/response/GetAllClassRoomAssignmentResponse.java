package hello.cluebackend.domain.assignment.management.api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetAllClassRoomAssignmentResponse {
  private Long assignmentId;
  private boolean allSubmitted;
  private String title;
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  public GetAllClassRoomAssignmentResponse(Long assignmentId, String title, LocalDateTime startDate, LocalDateTime endDate){
    this.assignmentId = assignmentId;
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
//    this.allSubmitted = //새로운 sql 호출
  }
}
