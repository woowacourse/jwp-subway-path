package subway.service.dto;

import java.util.List;

public class ShortestRouteResponse {

    private final List<String> stations;
    private final int cost;
    private final int distance;

    public ShortestRouteResponse(final List<String> stations, final int cost, final int distance) {
        this.stations = stations;
        this.cost = cost;
        this.distance = distance;
    }

    public List<String> getStations() {
        return stations;
    }

    public int getCost() {
        return cost;
    }

    public int getDistance() {
        return distance;
    }
}
