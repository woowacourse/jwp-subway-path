package subway.controller.dto.response;

import java.util.List;

public class ShortestPathResponse {

    private final int distance;
    private final int fee;
    private final List<StationResponse> route;

    public ShortestPathResponse(final int distance, final int fee, final List<StationResponse> route) {
        this.distance = distance;
        this.fee = fee;
        this.route = route;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }

    public List<StationResponse> getRoute() {
        return route;
    }
}
