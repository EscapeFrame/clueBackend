package hello.cluebackend.assignment.api.dto.response;

import java.time.LocalDateTime;

public record GetAssignmentResponseDto(
        Long assignmentId,
        String title,
        String content,
        LocalDateTime endDate,
        String duringTime
) {}
