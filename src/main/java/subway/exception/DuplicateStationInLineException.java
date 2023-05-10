package subway.exception;

public class DuplicateStationInLineException extends RuntimeException {

    public DuplicateStationInLineException(String message) {
        super(message);
    }
}
