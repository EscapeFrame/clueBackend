package hello.cluebackend.application.assignment.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CreateAssignmentDto (
        @NotNull @JsonProperty("class_id") UUID classId,
        @NotNull String title,
        @NotNull String content,
        @NotNull @JsonProperty("start_date") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul") LocalDateTime startDate,
        @NotNull @JsonProperty("end_date") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul") LocalDateTime endDate
){}