package subway.domain;

import java.util.List;

public class LineSegment {
    private final Long lineId;
    private final List<StationEdge> stationEdges;

    public LineSegment(Long lineId, List<StationEdge> stationEdges) {
        this.lineId = lineId;
        this.stationEdges = stationEdges;
    }

    public Distance calculateDistance() {
        return stationEdges
                .stream()
                .map(StationEdge::getDistance)
                .reduce(Distance.from(0), Distance::plus);
    }
}
