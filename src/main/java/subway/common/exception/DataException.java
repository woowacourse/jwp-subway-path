package subway.common.exception;

public class DataException extends ServerErrorException {
    public static final String MESSAGE = "Resultset을 객체로 매핑하는 데에 실패했습니다.";

    public DataException() {
        super(MESSAGE);
    }

    public DataException(Throwable cause) {
        super(MESSAGE, cause);
    }

    protected DataException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE, cause, enableSuppression, writableStackTrace);
    }
}
