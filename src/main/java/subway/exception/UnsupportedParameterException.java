package subway.exception;

public class UnsupportedParameterException extends RuntimeException {

    public UnsupportedParameterException() {
        super("해당 기능을 실행할 수 없는 파라미터입니다.");
    }

    public UnsupportedParameterException(final String message) {
        super(message);
    }
}
