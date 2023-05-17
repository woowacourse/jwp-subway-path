package subway.domain.path;

import java.util.List;
import subway.domain.Station;
import subway.domain.section.Section;

public interface ShortestPathFinder {

    List<Section> findShortestPath(final List<Section> sections, final Station startStation, Station endStation);
}
