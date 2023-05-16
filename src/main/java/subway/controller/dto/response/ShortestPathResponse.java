package subway.controller.dto.response;

import java.util.List;

public class ShortestPathResponse {

    private List<StationResponse> path;
    private long distance;
    private long fare;

    public ShortestPathResponse(final List<StationResponse> path, final long distance, final long fare) {
        this.path = path;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public long getDistance() {
        return distance;
    }

    public long getFare() {
        return fare;
    }
}
