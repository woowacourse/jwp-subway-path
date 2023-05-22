package subway.domain.subway;

import java.util.List;
import subway.domain.section.PathSection;
import subway.domain.station.Station;

public interface SubwayGraph {

    List<PathSection> findShortestPathSections(final Station start, final Station end);

    long calculateShortestDistance(final Station start, final Station end);
}
