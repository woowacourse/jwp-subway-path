package subway.domain.line.dto;

import subway.domain.line.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> path;
    private double distance;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(final List<StationResponse> path, final double distance, final int fare) {
        this.path = path;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse from(Path path) {
        List<StationResponse> stations = path.getPath().stream()
                .map(p -> new StationResponse(p.getId(), p.getName()))
                .collect(Collectors.toList());
        return new PathResponse(stations, path.getDistance(), path.getFare());
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public double getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
