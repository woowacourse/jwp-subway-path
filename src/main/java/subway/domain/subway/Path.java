package subway.domain.subway;

import java.util.List;
import subway.domain.station.Station;

public class Path {

    private final long distance;
    private List<Station> stations;

    public Path(final long distance, final List<Station> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public long getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }
}
