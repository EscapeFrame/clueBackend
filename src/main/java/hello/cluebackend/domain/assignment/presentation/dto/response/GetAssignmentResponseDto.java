package hello.cluebackend.domain.assignment.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record GetAssignmentResponseDto(
        Long assignmentId,
        String title,
        LocalDateTime endDate,
        String duringDate,
        List<Assignmentfile> files
) {}
