package hello.cluebackend.domain.assignment.presentation.dto.response;

import java.time.LocalDateTime;

public record StudentAssignmentRemain(
        String title,
        LocalDateTime duration,
        Long assignmentId
) {}
