package subway.dto.path;

import java.util.List;
import subway.dto.station.StationSelectResponse;

public class ShortestPathSelectResponse {
    private final List<StationSelectResponse> path;
    private final int distance;
    private final int fare;

    public ShortestPathSelectResponse(final List<StationSelectResponse> path, final int distance, final int fare) {
        this.path = path;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationSelectResponse> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
