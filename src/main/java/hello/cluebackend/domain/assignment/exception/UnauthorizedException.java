package hello.cluebackend.domain.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // 403 Forbidden 응답
public class UnauthorizedException extends RuntimeException {
  public UnauthorizedException() {
    super("접근 권한이 없습니다.");
  }

  public UnauthorizedException(String message) {
    super(message);
  }
}
