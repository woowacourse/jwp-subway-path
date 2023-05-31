package subway.exception;

public class StationNotFoundException extends SubwayException {

    public StationNotFoundException() {
        super("해당 역이 존재하지 않습니다.");
    }

    public StationNotFoundException(String message) {
        super(message);
    }
}
