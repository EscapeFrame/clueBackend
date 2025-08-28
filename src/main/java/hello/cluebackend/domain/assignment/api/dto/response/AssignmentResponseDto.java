package hello.cluebackend.domain.assignment.api.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AssignmentResponseDto(
        Long assignmentId,
        String title,
        String content,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String userName,

        // 과제 첨부 데이터
        List<AssignmentAttachmentDto> xAssignmentResponseDtos
) { }