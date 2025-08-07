package hello.cluebackend.assignment.api.dto.response;

import java.time.LocalDateTime;

public record GetAllAssignment(
        Long assignmentId,
        String title,
        LocalDateTime endDate,
        String duringTime
){}