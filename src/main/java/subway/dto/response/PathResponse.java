package subway.dto.response;

import java.util.List;

public class PathResponse {

    private final List<StationResponse> orderedStations;
    private final int distance;

    public PathResponse(List<StationResponse> orderedStations, int distance) {
        this.orderedStations = orderedStations;
        this.distance = distance;
    }

    public List<StationResponse> getOrderedStations() {
        return orderedStations;
    }

    public int getDistance() {
        return distance;
    }
}
