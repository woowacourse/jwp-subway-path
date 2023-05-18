package subway.exception.line;

public class InvalidDistanceException extends LineException {

    private final static String INVALID_DISTANCE_MESSAGE = "새로운 구간의 길이는 기존의 구간보다 길면 안된다.";

    public InvalidDistanceException() {
        super(INVALID_DISTANCE_MESSAGE);
    }
}
