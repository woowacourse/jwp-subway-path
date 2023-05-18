package subway.exception.line;

public class AlreadyExistLineException extends LineException {

    private static final String ALREADY_LINE_EXIST_MESSAGE = "이미 존재하는 노선입니다.";

    public AlreadyExistLineException() {
        super(ALREADY_LINE_EXIST_MESSAGE);
    }
}
