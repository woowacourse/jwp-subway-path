package subway.domain;

import java.util.List;

public class ShortestPath {

    private final List<Station> stations;
    private final Long shortestDistance;

    public ShortestPath(final List<Station> stations, final Long shortestDistance) {
        this.stations = stations;
        this.shortestDistance = shortestDistance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Long getShortestDistance() {
        return shortestDistance;
    }
}
