package subway.dto;

import subway.domain.Fare;
import subway.domain.ShortestPath;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ShortestPathResponse {

    private final List<StationResponse> routes;
    private final int distance;
    private final int fare;

    private ShortestPathResponse(List<StationResponse> routes, int distance, int fare) {
        this.routes = routes;
        this.distance = distance;
        this.fare = fare;
    }

    public static ShortestPathResponse from(ShortestPath shortestPath, Fare fare) {
        final List<StationResponse> routes = shortestPath.getRoutes().stream()
                .map(StationResponse::of)
                .collect(toList());

        return new ShortestPathResponse(routes, shortestPath.getDistance().getDistance(), fare.getFare());
    }

    public List<StationResponse> getRoutes() {
        return routes;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
