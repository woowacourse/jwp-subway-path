package subway.domain.path;

import subway.domain.Sections;
import subway.domain.Station;

public interface ShortestPathFinder {

    Path find(final Sections allSections, final Station startStation, final Station endStation);
}
