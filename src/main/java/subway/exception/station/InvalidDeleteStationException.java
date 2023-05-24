package subway.exception.station;

import subway.exception.SubwayGlobalException;

public class InvalidDeleteStationException extends SubwayGlobalException {
    public InvalidDeleteStationException(final String message) {
        super(message);
    }
}
