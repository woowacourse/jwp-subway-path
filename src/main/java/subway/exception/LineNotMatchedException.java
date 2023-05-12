package subway.exception;

public class LineNotMatchedException extends RuntimeException {

    public LineNotMatchedException() {
        super("올바르지 않은 라인입니다.");
    }
}
