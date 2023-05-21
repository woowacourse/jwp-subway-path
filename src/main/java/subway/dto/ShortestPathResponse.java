package subway.dto;

import java.util.List;
import subway.domain.Path;

public class ShortestPathResponse {

    private final List<String> path;
    private final int distance;
    private final int fare;

    public ShortestPathResponse(final List<String> path, final int distance, final int fare) {
        this.path = path;
        this.distance = distance;
        this.fare = fare;
    }

    public static ShortestPathResponse of(final Path path, final int fare) {
        return new ShortestPathResponse(path.getAllStationName(), path.getDistance().getValue(), fare);
    }

    public List<String> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
