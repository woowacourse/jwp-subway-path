package subway.exception.station;

import subway.exception.SubwayGlobalException;

public class InvalidStationNameLengthException extends SubwayGlobalException {

    public InvalidStationNameLengthException(final String message) {
        super(message);
    }

}
