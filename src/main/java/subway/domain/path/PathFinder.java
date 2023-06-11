package subway.domain.path;

import subway.domain.station.Station;

public interface PathFinder {

    Path findShortestPath(final Station start, final Station end);
}
