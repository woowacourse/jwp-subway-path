package subway.exception;

public class InvalidLineException extends RuntimeException {
    public InvalidLineException() {
        super("유효하지 않은 호선입니다.");
    }
}
