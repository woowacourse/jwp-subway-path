package subway.exception.station;

public class AlreadyExistStationException extends StationException {

    public AlreadyExistStationException() {
        super("이미 존재하는 지하철역입니다.");
    }
}
