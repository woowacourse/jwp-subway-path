package subway.application.core.exception;

public class FareCantCalculatedException extends ExpectedException {

    private static final String MESSAGE = "해당하는 거리에 맞는 요금을 찾을 수 없습니다.";

    public FareCantCalculatedException() {
        super(MESSAGE);
    }
}
