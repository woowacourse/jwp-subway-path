package subway.dto;

import java.util.List;

public class ShortestPathResponse {

    private List<StationResponse> path;
    private int distance;
    private int fare;

    public ShortestPathResponse(final List<StationResponse> path, final int distance, final int fare) {
        this.path = path;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
