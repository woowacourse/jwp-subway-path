package subway.exception;

public class IllegalDistanceException extends SubwayException {

    public IllegalDistanceException() {
        super("역 사이 거리는 0km이상 100km 이하여야 합니다.");
    }
}
