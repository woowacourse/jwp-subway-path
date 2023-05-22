package subway.exception.common;

public class NotFoundLineException extends NotFoundException {

    private static final String LINE_NOT_FOUND_MESSAGE = "해당 노선이 존재하지 않습니다.";

    public NotFoundLineException() {
        super(LINE_NOT_FOUND_MESSAGE);
    }
}
