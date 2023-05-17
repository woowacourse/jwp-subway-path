package subway.exception;

public class AlreadyExistStationException extends SubwayException {

    public AlreadyExistStationException() {
        super("이미 존재하는 지하철역입니다.");
    }
}
