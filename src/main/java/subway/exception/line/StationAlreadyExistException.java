package subway.exception.line;

public class StationAlreadyExistException extends LineException {
    private static final String MESSAGE = "이미 존재하는 역입니다.";

    public StationAlreadyExistException() {
        super(MESSAGE);
    }
}
