package subway.exception;

public class DataConstraintViolationException extends RuntimeException {

    public DataConstraintViolationException() {
        super("다른 테이블에서 해당 레코드를 참조 중 입니다.");
    }

    public DataConstraintViolationException(final String message) {
        super(message);
    }
}
