package subway.exception.section;

import subway.exception.SubwayGlobalException;

public class AlreadyConnectedSectionException extends SubwayGlobalException {
    public AlreadyConnectedSectionException(final String message) {
        super(message);
    }
}
