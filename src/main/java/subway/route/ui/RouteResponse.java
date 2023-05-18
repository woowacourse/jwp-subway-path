package subway.route.ui;

import subway.route.application.dto.RouteDto;

import java.util.List;
import java.util.stream.Collectors;

class RouteResponse {

    private final List<RouteSegmentResponse> route;
    private final int fare;

    RouteResponse(List<RouteSegmentResponse> route, int fare) {
        this.route = route;
        this.fare = fare;
    }

    public static RouteResponse from(RouteDto routeDto) {
        final List<RouteSegmentResponse> route = routeDto.getRoute()
                                                         .stream()
                                                         .map(RouteSegmentResponse::from)
                                                         .collect(Collectors.toUnmodifiableList());

        return new RouteResponse(route, routeDto.getFare());
    }

    public List<RouteSegmentResponse> getRoute() {
        return route;
    }

    public int getFare() {
        return fare;
    }
}
