package subway.service.dto;

import java.util.List;

public class PathResponse {

    private final List<StationResponse> stationResponses;
    private final int totalFare;
    private final int totalDistance;

    public PathResponse(final List<StationResponse> stationResponses, final int totalFare, final int totalDistance) {
        this.stationResponses = stationResponses;
        this.totalFare = totalFare;
        this.totalDistance = totalDistance;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public int getTotalFare() {
        return totalFare;
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
