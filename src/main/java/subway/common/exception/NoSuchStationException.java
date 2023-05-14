package subway.common.exception;

public class NoSuchStationException extends ApiNoSuchResourceException {

    public NoSuchStationException() {
        super("존재하지 않는 역입니다.");
    }
}
