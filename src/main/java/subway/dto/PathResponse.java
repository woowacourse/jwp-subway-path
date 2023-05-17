package subway.dto;

import java.util.List;
import java.util.stream.Collectors;

import subway.domain.Station;

public class PathResponse {
    private final List<StationResponse> path;

    private PathResponse(List<StationResponse> path) {
        this.path = path;
    }

    public static PathResponse of(List<Station> path) {
        List<StationResponse> stationResponses = path.stream()
            .map(StationResponse::of)
            .collect(Collectors.toUnmodifiableList());
        return new PathResponse(stationResponses);
    }

    public List<StationResponse> getPath() {
        return path;
    }

}
