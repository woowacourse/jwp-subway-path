package subway.exception;

public class LineNotEmptyException extends SubwayException {

    public LineNotEmptyException() {
        super("노선이 비어있지 않습니다.");
    }
}
