package subway.exception;

public class NoDataFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "해당 데이터가 존재하지 않습니다.";

    public NoDataFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
