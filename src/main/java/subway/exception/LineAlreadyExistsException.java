package subway.exception;

public class LineAlreadyExistsException extends SubwayException {

    public LineAlreadyExistsException() {
        super("노선이 이미 존재합니다.");
    }
}
