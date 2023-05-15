package subway.exception;

public class NoSuchLineException extends IllegalArgumentException {
    public NoSuchLineException() {
        super("존재하지 않는 노선입니다.");
    }
}
