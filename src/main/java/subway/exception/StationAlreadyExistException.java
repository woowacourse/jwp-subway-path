package subway.exception;

public class StationAlreadyExistException extends RuntimeException {

    private static final String MESSAGE = "기존에 역이 존재합니다.";

    public StationAlreadyExistException() {
        super(MESSAGE);
    }
}
