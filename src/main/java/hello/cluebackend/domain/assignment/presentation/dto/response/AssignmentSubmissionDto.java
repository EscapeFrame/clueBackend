package hello.cluebackend.domain.assignment.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record AssignmentSubmissionDto(
        Long userId,
        int studentNumber,
        String userName,
        Boolean isSubmitted,
        LocalDateTime submittedAt,
        List<Long> assignmentAttachmentIds
) {}
