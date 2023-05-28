package subway.domain.route;

import java.util.List;
import subway.domain.Station;

public class Path {
    private final List<Station> path;
    private final int distance;

    public Path(final List<Station> path, final int distance) {
        this.path = path;
        this.distance = distance;
    }

    public List<Station> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }
}
