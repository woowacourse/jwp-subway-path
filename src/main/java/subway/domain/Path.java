package subway.domain;

import java.util.List;
import subway.domain.section.Distance;
import subway.domain.station.Station;

public final class Path {

    private final List<Station> stations;
    private final Distance distance;

    public Path(final List<Station> stations, final int distance) {
        this.stations = stations;
        this.distance = new Distance(distance);
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }
}
