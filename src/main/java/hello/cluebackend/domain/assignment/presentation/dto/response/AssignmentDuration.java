package hello.cluebackend.domain.assignment.presentation.dto.response;

import java.time.LocalDateTime;

public record AssignmentDuration(
  LocalDateTime startDate,
  LocalDateTime endDate
){}