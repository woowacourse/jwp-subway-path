package subway.exception.line;

public class LineAlreadyExistException extends LineException {
    private static final String MESSAGE = "이미 존재하는 노선입니다.";

    public LineAlreadyExistException() {
        super(MESSAGE);
    }
}
