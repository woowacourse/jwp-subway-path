package subway.domain.path;

import java.util.List;
import subway.domain.Line;
import subway.domain.Station;

public interface PathFinder {

    Path findShortestPath(final Station startStation, final Station endStation, final List<Line> lines);
}
