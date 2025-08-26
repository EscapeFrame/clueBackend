package hello.cluebackend.domain.assignment.api.dto.response;

import hello.cluebackend.domain.assignment.domain.fileType;
import lombok.Builder;

@Builder
public record AssignmentAttachmentDto(
        fileType type,
        String value,
        String originalFileName,
        String contentType,
        Long size
) {}
