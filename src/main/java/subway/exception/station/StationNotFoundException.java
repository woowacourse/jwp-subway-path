package subway.exception.station;

public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException() {
        super("해당 되는 역을 찾을 수 없습니다.");
    }
}
