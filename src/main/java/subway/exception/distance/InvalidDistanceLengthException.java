package subway.exception.distance;

import subway.exception.SubwayGlobalException;

public class InvalidDistanceLengthException extends SubwayGlobalException {
    public InvalidDistanceLengthException(final String message) {
        super(message);
    }
}
