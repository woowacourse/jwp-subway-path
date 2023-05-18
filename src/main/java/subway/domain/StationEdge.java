package subway.domain;

import subway.exception.InvalidDistanceException;

import java.util.List;

public class StationEdge {

    private final Long downStationId;
    private final int distance;

    public StationEdge(final Long downStationId, final int distance) {
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public List<StationEdge> splitFromDownStation(final Long insertStationId, final int distance) {
        final int distanceFromUpStation = calculateDistanceFromUpStation(distance);
        final StationEdge upStationEdge = new StationEdge(insertStationId, distanceFromUpStation);
        final StationEdge downStationEdge = new StationEdge(downStationId, distance);
        return List.of(upStationEdge, downStationEdge);
    }

    private int calculateDistanceFromUpStation(final int distanceFromDownStation) {
        if (distance == 0) {
            return 0;
        }

        final int distanceFromUpStation = distance - distanceFromDownStation;
        if (distanceFromUpStation <= 0) {
            throw new InvalidDistanceException();
        }
        return distanceFromUpStation;
    }

    public boolean isDownStationId(Long id) {
        return downStationId.equals(id);
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
