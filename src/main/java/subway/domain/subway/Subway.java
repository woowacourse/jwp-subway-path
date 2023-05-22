package subway.domain.subway;

import java.util.List;
import subway.domain.section.PathSection;
import subway.domain.station.Station;

public class Subway {

    private final SubwayGraph subwayGraph;

    public Subway(final SubwayGraph subwayGraph) {
        this.subwayGraph = subwayGraph;
    }

    public List<PathSection> findShortestPathSections(final Station start, final Station end) {
        return subwayGraph.findShortestPathSections(start, end);
    }

    public long calculateShortestDistance(final Station start, final Station end) {
        return subwayGraph.calculateShortestDistance(start, end);
    }
}
