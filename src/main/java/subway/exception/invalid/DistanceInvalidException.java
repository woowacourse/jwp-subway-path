package subway.exception.invalid;

public class DistanceInvalidException extends InvalidException {

    public DistanceInvalidException() {
        super("유효하지 않은 거리 값입니다.");
    }
}
