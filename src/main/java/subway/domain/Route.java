package subway.domain;

import java.util.List;

public class Route {

    private final List<Station> stations;
    private final int distance;

    public Route(final List<Station> stations, final int distance) {
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
