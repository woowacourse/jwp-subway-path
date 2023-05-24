package subway.exception.section;

import subway.exception.SubwayGlobalException;

public class DisconnectedSectionException extends SubwayGlobalException {
    public DisconnectedSectionException(final String message) {
        super(message);
    }
}
