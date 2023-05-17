package subway.exception;

public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException() {
        super("존재하지 않는 역입니다.");
    }
}
