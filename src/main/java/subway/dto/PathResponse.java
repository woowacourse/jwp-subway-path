package subway.dto;

import java.util.List;
import java.util.stream.Collectors;

import subway.domain.Path;

public class PathResponse {

    private final List<StationResponse> stations;
    private final Integer fare;
    private final Integer distance;

    private PathResponse(List<StationResponse> stations, Integer fare, Integer distance) {
        this.stations = stations;
        this.fare = fare;
        this.distance = distance;
    }

    public static PathResponse of(Path path) {
        return new PathResponse(
                path.getStations().stream()
                        .map(StationResponse::of)
                        .collect(Collectors.toList()),
                path.getDistance().calculateFare(),
                path.getDistance().getValue()
        );
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getFare() {
        return fare;
    }

    public Integer getDistance() {
        return distance;
    }
}
