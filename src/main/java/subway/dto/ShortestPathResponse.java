package subway.dto;

import java.util.List;

public class ShortestPathResponse {

    private final List<LineDto> path;
    private final int distance;
    private final int fare;

    public ShortestPathResponse(final List<LineDto> path, final int distance, final int fare) {
        this.path = path;
        this.distance = distance;
        this.fare = fare;
    }

    public List<LineDto> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
