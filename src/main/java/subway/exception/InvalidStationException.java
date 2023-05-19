package subway.exception;

public class InvalidStationException extends GlobalException {
    public InvalidStationException() {
        super("유효하지 않은 역입니다.");
    }
}
