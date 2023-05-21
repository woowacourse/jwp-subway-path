package subway.exception.section;

import subway.exception.SubwayGlobalException;

public class InvalidAddSectionLengthException extends SubwayGlobalException {
    public InvalidAddSectionLengthException(final String message) {
        super(message);
    }
}
