package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.fare.Fare;
import subway.domain.path.Path;
import subway.domain.station.Station;
import subway.domain.station.Stations;

public class PathResponse {

    private List<StationResponse> path;
    private Integer totalFare;
    private Integer totalDistance;

    public PathResponse() {
    }

    public PathResponse(
            final List<StationResponse> path,
            final Integer totalFare,
            final Integer totalDistance
    ) {
        this.path = path;
        this.totalFare = totalFare;
        this.totalDistance = totalDistance;
    }

    public static PathResponse from(final Path path, final Fare totalFare) {
        return new PathResponse(
                createAllStationResponse(path.getPathStations()),
                totalFare.getFare(),
                path.getPathDistance().distance()
        );
    }

    private static List<StationResponse> createAllStationResponse(final Stations stations) {
        return stations.stations().stream()
                .map(PathResponse::createStationResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    private static StationResponse createStationResponse(final Station station) {
        return new StationResponse(station.getId(), station.getName().name());
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public Integer getTotalFare() {
        return totalFare;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }
}
