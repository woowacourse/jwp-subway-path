package subway.service.dto;

import java.util.List;

public class PathResponse {

    private final List<StationResponse> pathStations;
    private final Integer totalFare;
    private final Integer totalDistance;

    private PathResponse() {
        this(null, null, null);
    }

    public PathResponse(final List<StationResponse> pathStations, final Integer totalFare,
                        final Integer totalDistance) {
        this.pathStations = pathStations;
        this.totalFare = totalFare;
        this.totalDistance = totalDistance;
    }

    public List<StationResponse> getPathStations() {
        return pathStations;
    }

    public int getTotalFare() {
        return totalFare;
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
