package hello.cluebackend.application.assignment.dto.response;

import hello.cluebackend.domain.assignment.model.AssignmentAttachment;
import hello.cluebackend.domain.assignment.model.FileType;

import java.util.UUID;

public record AssignmentAttachmentDto(
        UUID assignmentAttachmentId,
        FileType type,
        String value,
        String originalFileName,
        String contentType,
        Long size
) {
  public static AssignmentAttachmentDto from(AssignmentAttachment assignmentAttachment){
    return new AssignmentAttachmentDto(
      assignmentAttachment.getAssignmentAttachmentId(),
      assignmentAttachment.getType(),
      assignmentAttachment.getValue(),
      assignmentAttachment.getOriginalFileName(),
      assignmentAttachment.getContentType(),
      assignmentAttachment.getSize()
    );
  }
}
