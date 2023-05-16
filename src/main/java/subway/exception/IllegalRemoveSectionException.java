package subway.exception;

public class IllegalRemoveSectionException extends RuntimeException {

    private static final String MESSAGE = "구간 삭제 과정 중 예외가 발생했습니다.";

    public IllegalRemoveSectionException() {
        super(MESSAGE);
    }
}
