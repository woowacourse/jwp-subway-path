package subway.route.application.dto;

import subway.route.domain.RouteSegment;

import java.util.ArrayList;
import java.util.List;

public class RouteDto {

    private final List<RouteSegment> route;
    private final int fare;

    public RouteDto(List<RouteSegment> route, int fare) {
        this.route = new ArrayList<>(route);
        this.fare = fare;
    }

    public List<RouteSegment> getRoute() {
        return route;
    }

    public int getFare() {
        return fare;
    }
}
