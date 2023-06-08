package subway.domain;

import subway.exception.ErrorCode;
import subway.exception.InvalidException;

public interface PathFinder {
    Path findPath(Station sourceStation, Station targetStation);

    default void validateSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new InvalidException(ErrorCode.INVALID_SAME_UP_AND_DOWN_STATION);
        }
    }
}
