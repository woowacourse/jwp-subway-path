package subway.domain.path;

import subway.domain.Lines;
import subway.domain.Station;

public interface PathFinder {

    Path findShortestPath(final Station startStation, final Station endStation, final Lines lines);
}
