package hello.cluebackend.assignment.management.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AssignmentResult<T> {
  private T assignments;
}
