package subway.application.core.exception;

public class StationConnectException extends ExpectedException {

    private static final String MESSAGE = "노선과 연결되지 않는 역입니다.";

    public StationConnectException() {
        super(MESSAGE);
    }
}
