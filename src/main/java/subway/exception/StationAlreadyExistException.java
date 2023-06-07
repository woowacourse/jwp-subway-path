package subway.exception;

public class StationAlreadyExistException extends RuntimeException {
    public StationAlreadyExistException() {
        super("해당 역이 이미 존재 합니다.");
    }
}
