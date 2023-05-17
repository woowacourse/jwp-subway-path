package subway.domain;

import java.util.List;

public class ShortestRouteWithStation {
    private final List<Station> stations;
    private final double totalDistance;
    private final int totalFare;

    public ShortestRouteWithStation(List<Station> stations, double totalDistance, int totalFare) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.totalFare = totalFare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public int getTotalFare() {
        return totalFare;
    }
}
