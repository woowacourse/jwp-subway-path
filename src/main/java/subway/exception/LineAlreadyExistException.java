package subway.exception;

public class LineAlreadyExistException extends RuntimeException {

    public LineAlreadyExistException() {
        super("해당 노선이 이미 존재합니다.");
    }
}
