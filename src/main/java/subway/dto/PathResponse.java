package subway.dto;

import subway.domain.Path;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

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
        final List<String> stationNames = path.getPathStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        return new PathResponse(stationNames, path.getDistance(), path.getFee());
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
