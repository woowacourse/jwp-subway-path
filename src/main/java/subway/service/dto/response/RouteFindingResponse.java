package subway.service.dto.response;

import java.util.List;

public class RouteFindingResponse {

    private final List<String> route;
    private final double distance;
    private final int fare;

    public RouteFindingResponse(List<String> route, double distance, int fare) {
        this.route = route;
        this.distance = distance;
        this.fare = fare;
    }

    public List<String> getRoute() {
        return route;
    }

    public double getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

}
