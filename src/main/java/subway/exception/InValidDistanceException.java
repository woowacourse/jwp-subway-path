package subway.exception;

public class InValidDistanceException extends RuntimeException {
    public InValidDistanceException() {
        super("역간 거리는 10km이하 양의 정수만 가능합니다.");
    }
}
