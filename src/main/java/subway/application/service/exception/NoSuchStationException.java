package subway.application.service.exception;

public class NoSuchStationException extends SubwayNoSuchResourceException {

    public NoSuchStationException() {
        super("존재하지 않는 역입니다.");
    }
}
