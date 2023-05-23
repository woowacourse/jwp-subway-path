package subway.dto;

import java.util.List;

public class RoutesResponse {

    private List<StationResponse> stationResponses;
    private Double totalDistance;
    private Integer fare;

    public RoutesResponse() {
    }

    public RoutesResponse(final List<StationResponse> stationResponses, final Double totalDistance,
                          final Integer fare) {
        this.stationResponses = stationResponses;
        this.totalDistance = totalDistance;
        this.fare = fare;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public Integer getFare() {
        return fare;
    }
}
