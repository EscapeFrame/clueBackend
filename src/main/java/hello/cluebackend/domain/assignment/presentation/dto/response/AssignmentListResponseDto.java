package hello.cluebackend.domain.assignment.presentation.dto.response;

import java.time.LocalDateTime;

public record AssignmentListResponseDto(
        String title,
        String content,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String fileName,
        Long fileId
) {
}
