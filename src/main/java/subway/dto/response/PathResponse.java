package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Station;

public class PathResponse {

    private final int fare;
    private final int distance;
    private final List<String> stations;

    public PathResponse(final int fare, final int distance, final List<String> stations) {
        this.stations = stations;
        this.fare = fare;
        this.distance = distance;
    }

    public static PathResponse from(final int fare, final int distance, final List<Station> stations) {
        final List<String> stationNames = stations.stream()
                .map(Station::getName)
                .collect(Collectors.toUnmodifiableList());
        return new PathResponse(fare, distance, stationNames);
    }

    public int getFare() {
        return fare;
    }

    public int getDistance() {
        return distance;
    }

    public List<String> getStations() {
        return stations;
    }
}
