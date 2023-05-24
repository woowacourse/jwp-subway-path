package subway.exception.line;

public class ForkedRoadException extends LineException {
    private static final String MESSAGE = "해당 노선에 갈래길이 존재합니다. 확인해주세요.";

    public ForkedRoadException() {
        super(MESSAGE);
    }
}
