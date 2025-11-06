package hello.cluebackend.application.assignment.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyAssignmentDto {
  private String title;
  private String content;
  private @JsonProperty("start_date") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul") LocalDateTime startDate;
  private @JsonProperty("end_date") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul") LocalDateTime endDate;
}