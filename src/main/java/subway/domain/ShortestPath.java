package subway.domain;

import java.util.List;

public class ShortestPath {

    private final List<Station> stations;

    public ShortestPath(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }
}
