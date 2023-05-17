package subway.dto;

import java.util.List;
import java.util.stream.Collectors;

import subway.domain.Station;

public class PathResponse {
    private final int distance;
    private final List<StationResponse> path;

    private PathResponse(int distance, List<StationResponse> path) {
        this.distance = distance;
        this.path = path;
    }

    public static PathResponse of(int distance, List<Station> path) {
        List<StationResponse> stationResponses = path.stream()
            .map(StationResponse::of)
            .collect(Collectors.toUnmodifiableList());
        return new PathResponse(distance, stationResponses);
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getPath() {
        return path;
    }

}
