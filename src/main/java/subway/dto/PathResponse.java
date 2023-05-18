package subway.dto;

import java.util.List;
import java.util.stream.Collectors;

import subway.domain.Path;

public class PathResponse {

    private final List<StationResponse> stations;
    private final Integer fare;

    public PathResponse(List<StationResponse> stations, Integer fare) {
        this.stations = stations;
        this.fare = fare;
    }

    public static PathResponse of(Path path) {
        return new PathResponse(
                path.getStations().stream()
                        .map(StationResponse::of)
                        .collect(Collectors.toList()),
                path.calculateFare()
        );
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getFare() {
        return fare;
    }
}
