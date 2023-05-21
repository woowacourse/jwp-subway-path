package subway.application.port.in.route.dto.response;

import java.util.List;
import subway.application.port.in.station.dto.response.StationQueryResponse;

public class RouteQueryResponse {

    private List<StationQueryResponse> route;
    private int distance;
    private int fare;

    private RouteQueryResponse() {
    }

    public RouteQueryResponse(final List<StationQueryResponse> route, final int distance, final int fare) {
        this.route = route;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationQueryResponse> getRoute() {
        return route;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
