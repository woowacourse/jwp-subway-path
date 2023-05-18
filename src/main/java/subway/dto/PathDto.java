package subway.dto;

import subway.dto.response.StationResponse;

import java.util.List;

public class PathDto {
    private final List<StationResponse> path;
    private final double distance;

    public PathDto(List<StationResponse> path, double distance) {
        this.path = path;
        this.distance = distance;
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public double getDistance() {
        return distance;
    }
}
