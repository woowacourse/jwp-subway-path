package subway.exception;

public class PathNotExistsException extends RuntimeException {
    public PathNotExistsException() {
        super("경로가 존재하지 않습니다.");
    }
}
