package hello.cluebackend.application.assignment.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyAssignmentDto {
  @NotNull
  private String title;
  @NotNull
  private String content;
  @NotNull
  private @JsonProperty("start_date") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul") LocalDateTime startDate;
  @NotNull
  private @JsonProperty("end_date") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul") LocalDateTime endDate;
}