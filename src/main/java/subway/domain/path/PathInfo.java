package subway.domain.path;

import java.util.List;
import java.util.Set;

public class PathInfo {
    private final List<Long> path;
    private final Set<Long> lineIds;
    private final int distance;

    public PathInfo(List<Long> path, Set<Long> lineIds, int distance) {
        this.path = path;
        this.lineIds = lineIds;
        this.distance = distance;
    }

    public List<Long> getPath() {
        return path;
    }

    public Set<Long> getLineIds() {
        return lineIds;
    }

    public int getDistance() {
        return distance;
    }
}
