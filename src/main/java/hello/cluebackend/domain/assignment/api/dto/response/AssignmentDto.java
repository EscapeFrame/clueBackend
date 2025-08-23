package hello.cluebackend.domain.assignment.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class AssignmentDto {
  private Long assignmentId;
  private String title;
  private String content;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm") private LocalDateTime startDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm") private LocalDateTime endDate;

  public AssignmentDto(Long assignmentId, String title, String content, LocalDateTime startDate, LocalDateTime endDate) {
    this.assignmentId = assignmentId;
    this.title = title;
    this.content = content;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}
