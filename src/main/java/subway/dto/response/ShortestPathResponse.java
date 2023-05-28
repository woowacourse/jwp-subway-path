package subway.dto.response;

import java.util.List;

public class ShortestPathResponse {

    private List<LineStationResponse> route;
    private Integer totalDistance;
    private Integer totalFare;

    ShortestPathResponse() {

    }

    public ShortestPathResponse(final List<LineStationResponse> route, final Integer totalDistance,
        final Integer totalFare) {
        this.route = route;
        this.totalDistance = totalDistance;
        this.totalFare = totalFare;
    }

    public List<LineStationResponse> getRoute() {
        return route;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public Integer getTotalFare() {
        return totalFare;
    }
}
