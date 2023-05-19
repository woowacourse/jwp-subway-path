package subway.controller.dto;

import java.util.List;

public class RouteSearchResponse {

    private List<StationResponse> routes;
    private Integer distance;
    private Integer fare;

    private RouteSearchResponse() {
    }

    public RouteSearchResponse(List<StationResponse> routes, Integer distance, Integer fare) {
        this.routes = routes;
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
