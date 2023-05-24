package subway.dto;

import subway.domain.Station;
import subway.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathDto {
    private final List<StationResponse> path;
    private final double distance;

    public PathDto(List<Station> shortestPath, double distance) {
        this.path = shortestPath.stream().map(StationResponse::of)
                .collect(Collectors.toList());
        this.distance = distance;
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public double getDistance() {
        return distance;
    }
}
