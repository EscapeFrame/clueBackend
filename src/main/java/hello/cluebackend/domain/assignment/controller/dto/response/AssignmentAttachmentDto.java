package hello.cluebackend.domain.assignment.controller.dto.response;

import hello.cluebackend.domain.assignment.domain.AssignmentAttachment;
import hello.cluebackend.domain.assignment.domain.FileType;

public record AssignmentAttachmentDto(
        FileType type,
        String value,
        String originalFileName,
        String contentType,
        Long size
) {
  public static AssignmentAttachmentDto from(AssignmentAttachment assignmentAttachment){
    return new AssignmentAttachmentDto(
      assignmentAttachment.getType(),
      assignmentAttachment.getValue(),
      assignmentAttachment.getOriginalFileName(),
      assignmentAttachment.getContentType(),
      assignmentAttachment.getSize()
    );
  }
}
