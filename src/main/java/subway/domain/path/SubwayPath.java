package subway.domain.path;

import java.util.List;
import subway.domain.station.Station;

public class SubwayPath {
    private final List<Station> stations;
    private final int distance;

    public SubwayPath(final List<Station> stations, final int distance) {
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
