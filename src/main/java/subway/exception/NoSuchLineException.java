package subway.exception;

public class NoSuchLineException extends ApiNoSuchResourceException {

    public NoSuchLineException() {
        super("존재하지 않는 노선입니다.");
    }

    public NoSuchLineException(Long lineId) {
        super("존재하지 않는 노선입니다. lineId : " + lineId);
    }
}
