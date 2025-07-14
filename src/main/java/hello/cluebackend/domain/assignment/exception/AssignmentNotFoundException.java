package hello.cluebackend.domain.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found 응답
public class AssignmentNotFoundException extends RuntimeException {
  public AssignmentNotFoundException() {
    super("해당 과제를 찾을 수 없습니다.");
  }

  public AssignmentNotFoundException(String message) {
    super(message);
  }
}
