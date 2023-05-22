package subway.exception.distance;

public class InvalidDistanceException extends DistanceException {

    private final static String INVALID_DISTANCE_MESSAGE = "거리는 음수가 될 수 없습니다.";

    public InvalidDistanceException() {
        super(INVALID_DISTANCE_MESSAGE);
    }
}
