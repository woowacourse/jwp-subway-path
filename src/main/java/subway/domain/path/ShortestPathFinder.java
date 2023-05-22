package subway.domain.path;

import java.util.List;
import subway.domain.Station;
import subway.domain.section.Section;

public interface ShortestPathFinder {
    Path findShortestPath(final List<Section> sections, final Station startStation, final Station endStation);
}
