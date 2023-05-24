package subway.exception.line;

import subway.exception.SubwayGlobalException;

public class InvalidLineNameLengthException extends SubwayGlobalException {
    public InvalidLineNameLengthException(final String message) {
        super(message);
    }
}
