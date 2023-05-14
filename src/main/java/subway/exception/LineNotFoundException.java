package subway.exception;

public class LineNotFoundException extends RuntimeException {

    private static final String MESSAGE = "호선이 존재하지 않습니다.";

    public LineNotFoundException() {
        super(MESSAGE);
    }
}
