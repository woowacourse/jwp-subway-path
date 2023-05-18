package subway.application.core.exception;

public class StationNotExistsException extends ExpectedException {

    private static final String MESSAGE = "존재하지 않는 역에 대해 삭제할 수 없습니다.";

    public StationNotExistsException() {
        super(MESSAGE);
    }
}
