package subway.exception;

public class DistanceNotValidException extends SubwayException {

    public DistanceNotValidException() {
        super("거리 값은 양수여야 합니다.");
    }
}
