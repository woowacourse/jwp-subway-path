package subway.application.core.exception;

public class StationAlreadyExistsException extends ExpectedException {

    private static final String MESSAGE = "이미 노선에 등록된 역입니다.";

    public StationAlreadyExistsException() {
        super(MESSAGE);
    }
}
