package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Distance;
import subway.domain.Station;

public class RouteResponse {

    private List<StationResponse> route;
    private int distance;

    public RouteResponse(List<StationResponse> route, int distance) {
        this.route = route;
        this.distance = distance;
    }

    public static RouteResponse of(List<Station> stations, Distance distance) {
        List<StationResponse> route = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new RouteResponse(route, distance.getValue());
    }

    public List<StationResponse> getRoute() {
        return route;
    }

    public int getDistance() {
        return distance;
    }
}
