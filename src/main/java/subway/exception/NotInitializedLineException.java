package subway.exception;

public final class NotInitializedLineException extends RuntimeException {
    public NotInitializedLineException() {
        super("초기화되지 않은 노선입니다.");
    }
}
