package subway.exception;

public class IllegalStationNameException extends SubwayException {

    public IllegalStationNameException() {
        super("역 이름은 10자 이하여야 합니다.");
    }
}
