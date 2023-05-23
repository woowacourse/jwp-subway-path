package subway.domain.path;

import java.util.List;

import subway.domain.section.Distance;
import subway.domain.station.Station;

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
