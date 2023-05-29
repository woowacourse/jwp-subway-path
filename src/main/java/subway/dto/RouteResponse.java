package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Distance;
import subway.domain.Fare;
import subway.domain.Station;

public class RouteResponse {

    private List<StationResponse> route;
    private int distance;
    private int fare;

    public RouteResponse(List<StationResponse> route, int distance, int fare) {
        this.route = route;
        this.distance = distance;
        this.fare = fare;
    }
    
    public static RouteResponse of(List<Station> stations, Distance distance, Fare fare) {
        List<StationResponse> route = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new RouteResponse(route, distance.getValue(), fare.getValue());
    }

    public List<StationResponse> getRoute() {
        return route;
    }

    public int getDistance() {
        return distance;
    }
    
    public int getFare() {
        return fare;
    }
}
