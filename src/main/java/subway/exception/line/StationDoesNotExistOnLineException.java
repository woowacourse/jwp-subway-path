package subway.exception.line;

public class StationDoesNotExistOnLineException extends LineException {
    private static final String MESSAGE = "해당 역이 해당 노선에 존재하지 않습니다.";

    public StationDoesNotExistOnLineException() {
        super(MESSAGE);
    }
}
