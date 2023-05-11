package subway.domain;

import java.util.List;
import subway.exception.InvalidDistanceException;

public class StationEdge {

    private final Long downStationId;
    private final int distance;

    public StationEdge(Long downStationId, int distance) {
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public List<StationEdge> splitFromDownStation(Long insertStationId, int distance) {
        int distanceFromUpStation = calculateDistanceFromUpStation(distance);
        StationEdge upStationEdge = new StationEdge(insertStationId, distanceFromUpStation);
        StationEdge downStationEdge = new StationEdge(downStationId, distance);
        return List.of(upStationEdge, downStationEdge);
    }

    private int calculateDistanceFromUpStation(int distanceFromDownStation) {
        if (distance == 0) {
            return 0;
        }

        int distanceFromUpStation = distance - distanceFromDownStation;
        if (distanceFromUpStation <= 0) {
            throw new InvalidDistanceException();
        }
        return distanceFromUpStation;
    }
}
