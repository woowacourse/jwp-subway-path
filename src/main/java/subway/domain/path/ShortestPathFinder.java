package subway.domain.path;

import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

public interface ShortestPathFinder {

    Path find(final List<Section> allSections, final Station startStation, final Station endStation);
}
