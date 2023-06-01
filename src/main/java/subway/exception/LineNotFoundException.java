package subway.exception;

public class LineNotFoundException extends RuntimeException {

    public LineNotFoundException() {
        super("존재하지 않는 노선입니다.");
    }
}
