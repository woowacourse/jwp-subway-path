package subway.application.port.in.route.dto.response;

import java.util.List;
import subway.application.port.in.station.dto.response.StationQueryResponse;

public class RouteQueryResponse {

    private List<StationQueryResponse> route;
    private int distance;

    private RouteQueryResponse() {
    }

    public RouteQueryResponse(final List<StationQueryResponse> route, final int distance) {
        this.route = route;
        this.distance = distance;
    }

    public List<StationQueryResponse> getRoute() {
        return route;
    }

    public int getDistance() {
        return distance;
    }
}
