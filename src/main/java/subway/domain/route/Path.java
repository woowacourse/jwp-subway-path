package subway.domain.route;

import java.util.List;
import subway.domain.Station;

public class Path {
    private final List<Station> path;
    private final int shortestDistance;

    public Path(final List<Station> path, final int shortestDistance) {
        this.path = path;
        this.shortestDistance = shortestDistance;
    }

    public List<Station> getPath() {
        return path;
    }

    public int getShortestDistance() {
        return shortestDistance;
    }
}
