package subway.exception.custom;

public class SectionDistanceTooLongException extends RuntimeException {

    public SectionDistanceTooLongException(final String message) {
        super(message);
    }
}
