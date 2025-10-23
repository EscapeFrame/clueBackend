package hello.cluebackend.domain.notice.exception;

public class IsNotMyNoticeException extends RuntimeException {
    public IsNotMyNoticeException(String message) {
        super(message);
    }
}
