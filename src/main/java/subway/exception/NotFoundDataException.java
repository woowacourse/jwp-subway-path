package subway.exception;

public class NotFoundDataException extends RuntimeException {

    public NotFoundDataException() {
        super("데이터가 존재하지 않습니다.");
    }

    public NotFoundDataException(final String message) {
        super(message);
    }
}
