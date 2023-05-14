package subway.line.exception;

public class InvalidStationNameException extends RuntimeException {

    public InvalidStationNameException(String message) {
        super(message);
    }
}
