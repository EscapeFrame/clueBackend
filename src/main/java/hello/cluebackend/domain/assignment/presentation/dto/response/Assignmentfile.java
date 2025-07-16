package hello.cluebackend.domain.assignment.presentation.dto.response;

public record Assignmentfile(
        Long fileId,
        String fileName,
        int fileSize
) {}