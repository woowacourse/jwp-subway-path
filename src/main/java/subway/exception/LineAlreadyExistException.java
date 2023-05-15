package subway.exception;

public class LineAlreadyExistException extends GlobalException {

    private static final String messageFormat = "이미 노선이 존재합니다. 입력값 : %s";

    public LineAlreadyExistException(final String wrongLineName) {
        super(String.format(messageFormat, wrongLineName));
    }
}
