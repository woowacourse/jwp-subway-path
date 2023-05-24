package subway.domain;

import java.util.List;

public class PathSegment {
    private final Long lineId;
    private final List<StationEdge> stationEdges;

    public PathSegment(Long lineId, List<StationEdge> stationEdges) {
        this.lineId = lineId;
        this.stationEdges = stationEdges;
    }

    public Distance calculateDistance() {
        return stationEdges
                .stream()
                .map(StationEdge::getDistance)
                .reduce(Distance.from(0), Distance::plus);
    }

    public Long getLineId() {
        return lineId;
    }

    public List<StationEdge> getStationEdges() {
        return stationEdges;
    }
}
