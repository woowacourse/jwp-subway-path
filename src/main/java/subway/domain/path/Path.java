package subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Distance;
import subway.domain.Station;

public class Path {

    private final List<Station> path;
    private final Distance distance;

    public Path(final List<Station> path, final Distance distance) {
        this.path = path;
        this.distance = distance;
    }

    public List<String> getAllStationName() {
        return path.stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }

    public List<Station> getPath() {
        return path;
    }

    public int getDistance() {
        return distance.getValue();
    }
}
