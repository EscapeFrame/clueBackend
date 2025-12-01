package hello.cluebackend.application.submission.dto.response;

import hello.cluebackend.domain.submission.model.FileType;
import hello.cluebackend.domain.submission.model.SubmissionAttachment;
import lombok.Builder;

import java.util.UUID;

@Builder
public record SubmissionAttachmentResponse(
        UUID submissionAttachmentId,
        FileType type,
        String value,
        String originalFileName,
        String contentType,
        Long size,
        String downloadUrl
) {

  public static SubmissionAttachmentResponse from(SubmissionAttachment submissionAttachment, String downloadUrl){
    return new SubmissionAttachmentResponse(
      submissionAttachment.getSubmissionAttachmentId(),
      submissionAttachment.getType(),
      submissionAttachment.getValue(),
      submissionAttachment.getOriginalFileName(),
      submissionAttachment.getContentType(),
      submissionAttachment.getSize(),
      downloadUrl
    );
  }
}
