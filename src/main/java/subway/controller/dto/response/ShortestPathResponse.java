package subway.controller.dto.response;

import java.util.List;

public class ShortestPathResponse {

    private final int distance;
    private final int fee;
    private final List<StationResponse> path;

    public ShortestPathResponse(final int distance, final int fee, final List<StationResponse> path) {
        this.distance = distance;
        this.fee = fee;
        this.path = path;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }

    public List<StationResponse> getPath() {
        return path;
    }
}
