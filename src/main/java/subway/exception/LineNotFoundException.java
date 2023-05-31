package subway.exception;

public class LineNotFoundException extends SubwayException {

    public LineNotFoundException() {
        super("노선을 찾을 수 없습니다.");
    }
}
