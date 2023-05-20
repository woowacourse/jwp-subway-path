package subway.domain;

import java.util.List;

public final class ShortestPath {

    private final List<LineStation> path;
    private final int distance;

    public ShortestPath(final List<LineStation> path, final int distance) {
        this.path = path;
        this.distance = distance;
    }

    public List<LineStation> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }
}
