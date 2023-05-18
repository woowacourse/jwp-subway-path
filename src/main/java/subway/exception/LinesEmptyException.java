package subway.exception;

public class LinesEmptyException extends RuntimeException {

    public LinesEmptyException() {
        super("최단 경로를 조회할 수 없습니다.");
    }
}
