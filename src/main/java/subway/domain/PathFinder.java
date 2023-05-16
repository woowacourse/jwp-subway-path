package subway.domain;

import java.util.List;

public interface PathFinder {

    Path find(final Station startStation, final Station endStation, final List<Line> lines);
}
