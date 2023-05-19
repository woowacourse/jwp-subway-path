package subway.exception;

public class LineDuplicateException extends IllegalArgumentException {

    public LineDuplicateException(String exceptionMessage) {
        super(exceptionMessage);
    }

}
