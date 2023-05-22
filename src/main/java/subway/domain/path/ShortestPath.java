package subway.domain.path;

import subway.domain.Station;

import java.util.List;

public class ShortestPath {

    private final List<Station> stations;
    private final int distance;

    public ShortestPath(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
