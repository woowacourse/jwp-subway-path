package subway.exception;

public final class NoSuchLineException extends RuntimeException {
    public NoSuchLineException() {
        super("존재하지 않는 노선입니다.");
    }
}
