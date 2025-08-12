package hello.cluebackend.domain.assignment.management.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAllClassRoomAssignmentResponseResult<T> {
  private T assignments;
}
