package subway.exception;

public class DistanceValueInvalidException extends RuntimeException {

    public DistanceValueInvalidException() {
        super("역 사이의 거리는 1이상이 되어야합니다.");
    }
}
