package subway.dto;

import java.util.List;

public class PathResponse {
    private final List<String> pathStations;
    private final int distance;
    private final int fee;

    public PathResponse(final List<String> pathStations, final int distance, final int fee) {
        this.pathStations = pathStations;
        this.distance = distance;
        this.fee = fee;
    }

    public List<String> getPathStations() {
        return pathStations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }
}
