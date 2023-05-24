package subway.exception.line;

public class LineDoesNotExistException extends LineException {
    private static final String MESSAGE = "존재하지 않는 노선입니다.";

    public LineDoesNotExistException() {
        super(MESSAGE);
    }
}
