package subway.exception;

public class InValidDistanceException extends GlobalException {
    public InValidDistanceException() {
        super("역간 거리는 100km이하 양의 정수만 가능합니다.");
    }
}
