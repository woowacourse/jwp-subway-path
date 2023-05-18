package subway.dto;

import java.util.List;

public class PathResponse {
    private final List<StationResponse> paths;
    private final int fare;
    private final int distance;

    public PathResponse(final List<StationResponse> paths, final int fare, final int distance) {
        this.paths = paths;
        this.fare = fare;
        this.distance = distance;
    }

    public List<StationResponse> getPaths() {
        return paths;
    }

    public int getFare() {
        return fare;
    }

    public int getDistance() {
        return distance;
    }
}
