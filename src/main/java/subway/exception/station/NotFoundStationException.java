package subway.exception.station;

import subway.exception.SubwayGlobalException;

public class NotFoundStationException extends SubwayGlobalException {
    public NotFoundStationException(final String message) {
        super(message);
    }
}
