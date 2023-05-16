package subway.controller.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Path;
import subway.domain.subway.billing_policy.Fare;

public class SubwayShortestPathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    public SubwayShortestPathResponse(final List<StationResponse> stations, final int distance, final int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static SubwayShortestPathResponse of(Path path, Fare fare) {
        return new SubwayShortestPathResponse(
                generateStationResponses(path),
                path.getDistance().getValue(),
                fare.getValue()
        );
    }

    private static List<StationResponse> generateStationResponses(final Path path) {
        return path.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
