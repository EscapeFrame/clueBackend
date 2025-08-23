package hello.cluebackend.domain.assignment.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetSubmissionResultDto<T> {
  private T SubmissionAttachments;
}
