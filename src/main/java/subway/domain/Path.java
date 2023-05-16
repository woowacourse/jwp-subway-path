package subway.domain;

import java.util.List;
import subway.domain.vo.Distance;

public class Path {

    private final List<Station> stations;
    private final Distance distance;

    public Path(final List<Station> stations, final Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public long calculateCost() {
        return 0;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }
}
