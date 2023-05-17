package subway.dto;

import java.util.List;

public class RouteResponse {

    private final List<StationResponse> stations;
    private final int totalDistance;
    private final int totalFare;

    public RouteResponse(final List<StationResponse> stations, final int totalDistance, final int totalFare) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.totalFare = totalFare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getTotalFare() {
        return totalFare;
    }
}
