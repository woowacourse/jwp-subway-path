package subway.service.dto;

import java.util.List;

public class ShortestRouteResponse {

    private final List<String> stations;
    private final double cost;
    private final int distance;

    public ShortestRouteResponse(final List<String> stations, final double cost, final int distance) {
        this.stations = stations;
        this.cost = cost;
        this.distance = distance;
    }

    public List<String> getStations() {
        return stations;
    }

    public double getCost() {
        return cost;
    }

    public int getDistance() {
        return distance;
    }
}
