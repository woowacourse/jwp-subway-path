package subway.service.dto;

import java.util.List;

public class PathResponse {

    private final List<StationResponse> stationResponses;
    private final Integer totalFare;
    private final Integer totalDistance;

    private PathResponse() {
        this(null, null, null);
    }

    public PathResponse(final List<StationResponse> stationResponses, final Integer totalFare,
                        final Integer totalDistance) {
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
