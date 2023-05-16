package subway.domain.exception;

public class EmptyRoutedStationsSearchResult extends RuntimeException {

    public EmptyRoutedStationsSearchResult(final String message) {
        super(message);
    }
}
