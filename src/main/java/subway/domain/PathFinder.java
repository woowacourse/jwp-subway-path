package subway.domain;

import java.util.List;

public interface PathFinder {

    Path findShortestPath(final Station startStation, final Station endStation, final List<Line> lines);
}
