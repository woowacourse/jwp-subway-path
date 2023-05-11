package subway.exception;

public class LineDuplicatedException extends RuntimeException {

    public LineDuplicatedException() {
    }

    public LineDuplicatedException(String message) {
        super(message);
    }
}
