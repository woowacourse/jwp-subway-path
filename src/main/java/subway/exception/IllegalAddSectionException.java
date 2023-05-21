package subway.exception;

public class IllegalAddSectionException extends RuntimeException {

    static final String MESSAGE = "구간 추가 과정 중 예외가 발생했습니다.";

    public IllegalAddSectionException() {
        super(MESSAGE);
    }

    public IllegalAddSectionException(final String message) {
        super(message);
    }
}