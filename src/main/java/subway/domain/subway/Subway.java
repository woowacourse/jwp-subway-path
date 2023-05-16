package subway.domain.subway;

import subway.domain.station.Station;

public class Subway {

    private final SubwayGraph subwayGraph;

    public Subway(final SubwayGraph subwayGraph) {
        this.subwayGraph = subwayGraph;
    }

    public Path findShortestPath(final Station start, final Station end) {
        return new Path(
                subwayGraph.calculateShortestDistance(start, end),
                subwayGraph.findShortestPath(start, end)
        );
    }
}
