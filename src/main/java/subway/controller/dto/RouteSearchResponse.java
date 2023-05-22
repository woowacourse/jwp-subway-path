package subway.controller.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.line.Station;

public class RouteSearchResponse {

    private List<StationResponse> routes;
    private Integer distance;
    private Integer fare;

    private RouteSearchResponse() {
    }

    public RouteSearchResponse(List<Station> routes, Integer distance, Integer fare) {
        this.routes = routes
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getRoutes() {
        return routes;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getFare() {
        return fare;
    }
}
