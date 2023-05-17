package subway.domain.subway;

import subway.domain.station.Station;

public class Subway {

    private final SubwayGraph subwayGraph;

    public Subway(final SubwayGraph subwayGraph) {
        this.subwayGraph = subwayGraph;
    }

    public long calculateShortestDistance(final Station start, final Station end) {
        return subwayGraph.calculateShortestDistance(start, end);
    }
}
