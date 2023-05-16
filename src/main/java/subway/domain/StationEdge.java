package subway.domain;

import java.util.List;

public class StationEdge {

    private final Long downStationId;
    private final Distance distance;

    public StationEdge(Long downStationId, Distance distance) {
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public StationEdge(Long downStationId, int distance) {
        this.downStationId = downStationId;
        this.distance = new Distance(distance);
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Distance getDistance() {
        return distance;
    }

    public List<StationEdge> splitFromDownStation(Long insertStationId, Distance distance) {
        StationEdge upStationEdge = new StationEdge(insertStationId, this.distance.minus(distance));
        StationEdge downStationEdge = new StationEdge(downStationId, distance);
        return List.of(upStationEdge, downStationEdge);
    }
}
