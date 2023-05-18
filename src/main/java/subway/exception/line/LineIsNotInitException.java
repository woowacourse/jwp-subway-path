package subway.exception.line;

public class LineIsNotInitException extends LineException {

    private static final String LINE_IS_NOT_INIT_MESSAGE = "해당 노선은 비어있지 않습니다.";

    public LineIsNotInitException() {
        super(LINE_IS_NOT_INIT_MESSAGE);
    }
}
