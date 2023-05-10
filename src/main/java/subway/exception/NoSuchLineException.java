package subway.exception;

public class NoSuchLineException extends ApiNoSuchResourceException {

    public NoSuchLineException() {
        super("존재하지 않는 호선입니다.");
    }
}
