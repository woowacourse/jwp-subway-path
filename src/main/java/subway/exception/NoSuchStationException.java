package subway.exception;

public class NoSuchStationException extends ApiNoSuchResourceException {

    public NoSuchStationException() {
        super("존재하지 않는 역입니다.");
    }

    public NoSuchStationException(Long stationId) {
        super("존재하지 않는 역입니다. stationId : " + stationId);
    }
}
