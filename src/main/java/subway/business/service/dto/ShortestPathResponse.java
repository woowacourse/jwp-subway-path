package subway.business.service.dto;

import java.util.List;

public class ShortestPathResponse {
    private final List<String> stationNamesOfShortestPath;
    private final int totalDistance;
    private final int totalFare;

    public ShortestPathResponse(List<String> stationNamesOfShortestPath, int totalDistance, int totalFare) {
        this.stationNamesOfShortestPath = stationNamesOfShortestPath;
        this.totalDistance = totalDistance;
        this.totalFare = totalFare;
    }

    public List<String> getStationNamesOfShortestPath() {
        return stationNamesOfShortestPath;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getTotalFare() {
        return totalFare;
    }
}
