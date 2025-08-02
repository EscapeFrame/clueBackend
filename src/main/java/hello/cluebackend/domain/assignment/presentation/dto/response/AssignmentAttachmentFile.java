package hello.cluebackend.domain.assignment.presentation.dto.response;

public record AssignmentAttachmentFile(
        String OriginalFileName,
        String StoredFileName,
        String FilePath,
        int FileSize
) { }
