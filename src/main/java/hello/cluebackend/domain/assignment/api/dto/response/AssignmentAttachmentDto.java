package hello.cluebackend.domain.assignment.api.dto.response;

import hello.cluebackend.domain.assignment.domain.FileType;
import lombok.Builder;

@Builder
public record AssignmentAttachmentDto(
        FileType type,
        String value,
        String originalFileName,
        String contentType,
        Long size
) {}
