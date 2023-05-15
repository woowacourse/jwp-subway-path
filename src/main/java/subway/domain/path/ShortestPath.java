package subway.domain.path;

import java.util.List;
import subway.domain.Distance;
import subway.domain.Station;

public class ShortestPath {

    private final List<Station> stations;
    private final Distance distance;

    public ShortestPath(List<Station> stations, int distance) {
        this(stations, new Distance(distance));
    }

    public ShortestPath(List<Station> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> passingStation() {
        return List.copyOf(stations);
    }

    public int getDistance() {
        return distance.getValue();
    }
}
