package subway.domain;

import java.util.List;
import subway.exception.InvalidDistanceException;

public class StationEdge {

    private final Long downStationId;
    private final int distance;

    public StationEdge(final Long downStationId, final int distance) {
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
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
}
