package subway.exception.line;

public class StationDoesNotExistException extends LineException {
    private static final String MESSAGE = "존재하지 않는 역입니다.";

    public StationDoesNotExistException() {
        super(MESSAGE);
    }
}
