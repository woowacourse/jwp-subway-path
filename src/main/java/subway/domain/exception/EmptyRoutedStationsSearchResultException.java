package subway.domain.exception;

public class EmptyRoutedStationsSearchResultException extends RuntimeException {

    public EmptyRoutedStationsSearchResultException(final String message) {
        super(message);
    }
}
