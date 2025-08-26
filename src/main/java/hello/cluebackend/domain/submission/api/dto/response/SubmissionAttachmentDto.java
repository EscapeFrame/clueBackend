package hello.cluebackend.domain.submission.api.dto.response;

import hello.cluebackend.domain.submission.domain.fileType;
import lombok.Builder;

@Builder
public record SubmissionAttachmentDto(
        fileType type,
        String value,
        String originalFileName,
        String contentType,
        Long size
) { }
