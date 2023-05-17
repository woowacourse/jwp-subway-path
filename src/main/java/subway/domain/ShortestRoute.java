package subway.domain;

import java.util.List;

public class ShortestRoute {
    private final List<String> stationNamesInRoute;
    private final double totalDistance;

    public ShortestRoute(List<String> stationNamesInRoute, double totalDistance) {
        this.stationNamesInRoute = stationNamesInRoute;
        this.totalDistance = totalDistance;
    }

    public List<String> getStationNamesInRoute() {
        return stationNamesInRoute;
    }

    public double getTotalDistance() {
        return totalDistance;
    }
}
