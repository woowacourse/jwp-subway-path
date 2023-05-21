package subway.dto;

import java.util.List;
import subway.domain.Path;

public class ShortestPathResponse {

    private final List<String> path;
    private final int distance;

    public ShortestPathResponse(final List<String> path, final int distance) {
        this.path = path;
        this.distance = distance;
    }

    public static ShortestPathResponse from(Path path) {
        return new ShortestPathResponse(path.getAllStationName(), path.getDistance().getValue());
    }

    public List<String> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }
}
