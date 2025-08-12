package hello.cluebackend.assignment.management.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GetAllClassRoomAssignmentResponse {
  private Long assignmentId;
  private boolean allSubmit;
  private String title;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
