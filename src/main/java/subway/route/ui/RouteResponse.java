package subway.route.ui;

import subway.route.application.dto.RouteDto;
import subway.route.domain.RouteSegment;

import java.util.List;
import java.util.stream.Collectors;

class RouteResponse {

    private final List<RouteSegmentResponse> route;
    private final int fare;
    private final int totalDistance;

    RouteResponse(List<RouteSegmentResponse> route, int fare, int totalDistance) {
        this.route = route;
        this.fare = fare;
        this.totalDistance = totalDistance;
    }

    public static RouteResponse from(RouteDto routeDto) {
        final List<RouteSegmentResponse> route = routeDto.getRoute()
                                                         .stream()
                                                         .map(RouteSegmentResponse::from)
                                                         .collect(Collectors.toUnmodifiableList());

        final int totalDistance = routeDto.getRoute()
                                          .stream()
                                          .mapToInt(RouteSegment::getDistance)
                                          .sum();

        return new RouteResponse(route, routeDto.getFare(), totalDistance);
    }

    public List<RouteSegmentResponse> getRoute() {
        return route;
    }

    public int getFare() {
        return fare;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    @Override
    public String toString() {
        return "RouteResponse{" +
                "route=" + route +
                ", fare=" + fare +
                ", totalDistance=" + totalDistance +
                '}';
    }
}
