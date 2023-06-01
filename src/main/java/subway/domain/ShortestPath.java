package subway.domain;

import java.util.List;

public class ShortestPath {

    private final int distance;
    private final List<Station> stations;

    public ShortestPath(final int distance, final List<Station> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
