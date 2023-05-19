package subway.exception;

public class InvalidDistanceException extends IllegalArgumentException {

    private static final String MESSAGE = "구간 간격이 너무 큽니다.";

    public InvalidDistanceException() {
        super(MESSAGE);
    }
}
