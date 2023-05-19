package subway.domain;

import java.util.List;

public class PathInformation {
    private final Integer distance;
    private final List<Section> path;

    public PathInformation(Integer distance, List<Section> path) {
        this.distance = distance;
        this.path = path;
    }

    public Integer getDistance() {
        return distance;
    }

    public List<Section> getPath() {
        return path;
    }
}
