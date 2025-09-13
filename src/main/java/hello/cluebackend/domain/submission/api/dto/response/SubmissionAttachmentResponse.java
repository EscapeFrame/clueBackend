package hello.cluebackend.domain.submission.api.dto.response;

import hello.cluebackend.domain.submission.domain.FileType;
import hello.cluebackend.domain.submission.domain.SubmissionAttachment;
import lombok.Builder;

@Builder
public record SubmissionAttachmentResponse(
        FileType type,
        String value,
        String originalFileName,
        String contentType,
        Long size
) {
  public static SubmissionAttachmentResponse from(SubmissionAttachment submissionAttachment){
    return new SubmissionAttachmentResponse(
      submissionAttachment.getType(),
      submissionAttachment.getValue(),
      submissionAttachment.getOriginalFileName(),
      submissionAttachment.getContentType(),
      submissionAttachment.getSize()
    );
  }
}
