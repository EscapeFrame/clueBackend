package hello.cluebackend.domain.submission.presentation.dto.response;

import hello.cluebackend.domain.submission.domain.FileType;
import hello.cluebackend.domain.submission.domain.SubmissionAttachment;
import lombok.Builder;

import java.util.UUID;

@Builder
public record SubmissionAttachmentResponse(
        UUID submissionAttachmentId,
        FileType type,
        String value,
        String originalFileName,
        String contentType,
        Long size
) {
  public static SubmissionAttachmentResponse from(SubmissionAttachment submissionAttachment){
    return new SubmissionAttachmentResponse(
      submissionAttachment.getSubmissionAttachmentId(),
      submissionAttachment.getType(),
      submissionAttachment.getValue(),
      submissionAttachment.getOriginalFileName(),
      submissionAttachment.getContentType(),
      submissionAttachment.getSize()
    );
  }
}
