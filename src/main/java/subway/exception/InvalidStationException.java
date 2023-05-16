package subway.exception;

public class InvalidStationException extends RuntimeException {
    public InvalidStationException() {
        super("유효하지 않은 역입니다.");
    }
}
