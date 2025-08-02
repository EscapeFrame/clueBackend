package hello.cluebackend.domain.assignmentAttachment.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AssignmentUploadFileDto {
  private List<String> urls;
}
