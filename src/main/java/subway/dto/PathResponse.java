package subway.dto;

import java.util.List;
import java.util.stream.Collectors;

import subway.domain.subway.Station;

public class PathResponse {
    private final int distance;
    private final List<StationResponse> path;
    private final int fee;

    private PathResponse(int distance, List<StationResponse> path, int fee) {
        this.distance = distance;
        this.path = path;
        this.fee = fee;
    }

    public static PathResponse of(int distance, List<Station> path, int fee) {
        List<StationResponse> stationResponses = path.stream()
            .map(StationResponse::of)
            .collect(Collectors.toUnmodifiableList());
        return new PathResponse(distance, stationResponses, fee);
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public int getFee() {
        return fee;
    }
}
