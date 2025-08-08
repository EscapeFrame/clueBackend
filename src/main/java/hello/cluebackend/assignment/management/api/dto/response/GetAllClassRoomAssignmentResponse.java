package hello.cluebackend.assignment.management.api.dto.response;

import hello.cluebackend.assignment.management.domain.AssignmentAttachment.AssignmentAttachment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class GetAllClassRoomAssignmentResponse {
  private Long assignmentId;
  private boolean allSubmit;
  private String title;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private List<attachment> files;
}
