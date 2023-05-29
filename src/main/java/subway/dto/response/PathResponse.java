package subway.dto.response;

import java.util.List;

public class PathResponse {

    private final List<StationResponse> orderedStations;
    private final int distance;
    private final int fare;

    public PathResponse(List<StationResponse> orderedStations, int distance, int fare) {
        this.orderedStations = orderedStations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getOrderedStations() {
        return orderedStations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
