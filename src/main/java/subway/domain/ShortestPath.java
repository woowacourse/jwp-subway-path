package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShortestPath {
    private final List<Station> path;
    private final int distance;

    public ShortestPath(List<Station> path, int distance) {
        this.path = new ArrayList<>(path);
        this.distance = distance;
    }

    public List<Station> getPath() {
        return Collections.unmodifiableList(path);
    }

    public int getDistance() {
        return distance;
    }

}
