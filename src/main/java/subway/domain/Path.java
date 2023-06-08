package subway.domain;

import java.util.List;

public class Path {
    private final List<Station> path;
    private final int distance;

    public Path(List<Station> path, int distance) {
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
