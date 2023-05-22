package subway.domain.path;

import subway.domain.station.Station;

public interface PathFinder {

    Path findPath(final Station departure, final Station arrival);
}
