package subway.dto;

import java.util.List;

public class ShortestPathResponse {
    private List<PathStationResponse> path;
    private int totalDistance;
    private int fare;

    private ShortestPathResponse() {
    }

    public ShortestPathResponse(final List<PathStationResponse> path, final int totalDistance, final int fare) {
        this.path = path;
        this.totalDistance = totalDistance;
        this.fare = fare;
    }

    public List<PathStationResponse> getPath() {
        return path;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getFare() {
        return fare;
    }
}
