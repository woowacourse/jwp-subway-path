package subway.exception;

public class NoSuchStationException extends IllegalArgumentException {
    public NoSuchStationException() {
        super("존재하지 않는 역입니다.");
    }
}
