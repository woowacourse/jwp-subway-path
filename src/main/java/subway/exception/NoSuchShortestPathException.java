package subway.exception;

public final class NoSuchShortestPathException extends RuntimeException {
    public NoSuchShortestPathException() {
        super("최단경로를 찾지 못했습니다.");
    }
}
