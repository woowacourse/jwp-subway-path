package subway.exception.vo;

public class DistanceException extends VoException {
    private static final String MESSAGE = "거리는 음수가 될 수 없습니다.";

    public DistanceException() {
        super(MESSAGE);
    }
}
