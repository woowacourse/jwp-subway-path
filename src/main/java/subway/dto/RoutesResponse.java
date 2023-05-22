package subway.dto;

import java.util.List;

public class RoutesResponse {

    private List<StationResponse> stationResponses;
    private Double totalDistance;

    public RoutesResponse() {
    }

    public RoutesResponse(final List<StationResponse> stationResponses, final Double totalDistance) {
        this.stationResponses = stationResponses;
        this.totalDistance = totalDistance;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }
}
