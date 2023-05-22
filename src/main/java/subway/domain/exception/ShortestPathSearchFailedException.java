package subway.domain.exception;

public class ShortestPathSearchFailedException extends RuntimeException {

    public ShortestPathSearchFailedException() {
        super("최단 경로를 찾는 중 예외가 발생했습니다");
    }
}
