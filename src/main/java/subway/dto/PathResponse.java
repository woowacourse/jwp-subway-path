package subway.dto;

import subway.domain.Path;

import java.util.List;

public class PathResponse {
    private final List<String> pathStations;
    private final int distance;
    private final int fee;

    public PathResponse(final List<String> pathStations, final int distance, final int fee) {
        this.pathStations = pathStations;
        this.distance = distance;
        this.fee = fee;
    }

    public static PathResponse of(final Path path) {
        return new PathResponse(path.getPathStations(), path.getDistance(), path.getFee());
    }

    public List<String> getPathStations() {
        return pathStations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }
}
