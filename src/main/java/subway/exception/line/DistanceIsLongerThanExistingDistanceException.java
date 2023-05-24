package subway.exception.line;

public class DistanceIsLongerThanExistingDistanceException extends LineException {
    private static final String MESSAGE = "추가하려는 거리가 기존의 거리보다 깁니다.";

    public DistanceIsLongerThanExistingDistanceException() {
        super(MESSAGE);
    }
}
