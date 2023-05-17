package subway.application.port.in.route.dto.response;

import java.util.List;
import subway.application.port.in.station.dto.response.StationQueryResponse;

public class RouteQueryResponse {

    private final List<StationQueryResponse> route;
    private final int distance;

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
