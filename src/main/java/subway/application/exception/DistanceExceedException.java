package subway.application.exception;

public class DistanceExceedException extends ExpectedException {

    private static final String MESSAGE = "역 간의 거리는 양수여야 합니다.";

    public DistanceExceedException() {
        super(MESSAGE);
    }
}
