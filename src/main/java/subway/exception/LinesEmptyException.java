package subway.exception;

public class LinesEmptyException extends RuntimeException {

    public LinesEmptyException() {
        super("노선이 존재하지 않습니다.");
    }
}
