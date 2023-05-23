package subway.dto;

import java.util.List;

import subway.domain.station.Station;

public class PathSearchResponse {

    private final List<Station> path;
    private final int distance;
    private final int fare;

    public PathSearchResponse(final List<Station> path, final int distance, final int fare) {
        this.path = path;
        this.distance = distance;
        this.fare = fare;
    }

    public List<Station> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
