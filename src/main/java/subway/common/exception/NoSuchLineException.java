package subway.common.exception;

public class NoSuchLineException extends SubwayNoSuchResourceException {

    public NoSuchLineException() {
        super("존재하지 않는 노선입니다.");
    }
}
