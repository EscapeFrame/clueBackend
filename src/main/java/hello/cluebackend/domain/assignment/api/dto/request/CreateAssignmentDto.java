package hello.cluebackend.domain.assignment.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAssignmentDto (
        @NotNull @JsonProperty("class_id") Long classId,
        @NotNull String title,
        @NotNull String content,
        @NotNull @JsonProperty("start_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime startDate,
        @NotNull @JsonProperty("end_date") @JsonFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime endDate
){}