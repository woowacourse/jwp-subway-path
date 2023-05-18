package subway.domain;

import java.util.List;

public class Path {

    private final List<Station> stations;
    private final Distance distance;

    public Path(final List<Station> stations, final double distance) {
        this(stations, (int) distance);
    }

    public Path(final List<Station> stations, final int distance) {
        this(stations, new Distance(distance));
    }

    public Path(final List<Station> stations, final Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }
}
