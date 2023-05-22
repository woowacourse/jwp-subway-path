package subway.exception.line;

public class BothStationsAlreadyExistException extends LineException {
    private static final String MESSAGE = "해당 노선에 두 역이 모두 존재합니다.";

    public BothStationsAlreadyExistException() {
        super(MESSAGE);
    }
}
