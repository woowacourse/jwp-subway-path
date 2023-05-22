package subway.exception.line;

public class BothStationsDoNotExistException extends LineException {
    private static final String MESSAGE = "해당 노선에 두 역이 모두 존재하지 않습니다.";

    public BothStationsDoNotExistException() {
        super(MESSAGE);
    }
}
