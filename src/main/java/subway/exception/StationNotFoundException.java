package subway.exception;

public class StationNotFoundException extends SubwayException {

    public StationNotFoundException() {
        super("역을 찾을 수 없습니다.");
    }
}
