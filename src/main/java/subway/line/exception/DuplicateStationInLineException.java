package subway.line.exception;

public class DuplicateStationInLineException extends RuntimeException {

    public DuplicateStationInLineException(String message) {
        super(message);
    }
}
