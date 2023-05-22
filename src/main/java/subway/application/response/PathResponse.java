package subway.application.response;

import subway.domain.station.Station;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private final int fare;
    private final List<StationResponse> path;
    private final double distance;

    public PathResponse(final int fare, final double distance, final List<Station> stations) {
        this.fare = fare;
        this.path = stations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
        this.distance = distance;
    }

    public int getFare() {
        return fare;
    }

    public List<StationResponse> getPath() {
        return path;
    }

    public double getDistance() {
        return distance;
    }
}
