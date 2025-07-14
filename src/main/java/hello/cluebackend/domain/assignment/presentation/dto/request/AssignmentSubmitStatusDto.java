package hello.cluebackend.domain.assignment.presentation.dto.request;

import java.time.LocalDateTime;

public record AssignmentSubmitStatusDto(
        String userName,
        boolean isSubmitted,
        LocalDateTime submittedAt,
        Long userId,
        Long assignmentId
) {
}
