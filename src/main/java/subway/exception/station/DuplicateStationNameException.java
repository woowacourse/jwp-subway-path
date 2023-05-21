package subway.exception.station;

import subway.exception.SubwayGlobalException;

public class DuplicateStationNameException extends SubwayGlobalException {
    public DuplicateStationNameException(final String message) {
        super(message);
    }
}
