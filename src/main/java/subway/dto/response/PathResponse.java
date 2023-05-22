package subway.dto.response;

import java.util.List;

public class PathResponse {

    private List<StationResponse> path;
    private Integer totalFare;
    private Integer totalDistance;

    public PathResponse() {
    }

    public PathResponse(
            final List<StationResponse> path,
            final Integer totalFare,
            final Integer totalDistance
    ) {
        this.path = path;
        this.totalFare = totalFare;
        this.totalDistance = totalDistance;
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public Integer getTotalFare() {
        return totalFare;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }
}
