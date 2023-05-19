package subway.ui.route.find.dto;

import java.util.List;

public class RouteFindResponse {

    private long distance;
    private long fare;
    private List<RouteEdgeResponse> stations;

    private RouteFindResponse() {
    }

    public RouteFindResponse(final List<RouteEdgeResponse> stations, final long distance, final long fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public long getDistance() {
        return distance;
    }

    public long getFare() {
        return fare;
    }

    public List<RouteEdgeResponse> getStations() {
        return stations;
    }
}
