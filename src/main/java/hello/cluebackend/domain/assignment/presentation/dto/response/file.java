package hello.cluebackend.domain.assignment.presentation.dto.response;

public record file(
        Long fileId,
        String fileName,
        int fileSize
) {}
