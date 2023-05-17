package subway.domain.path;

import java.util.List;
import subway.domain.core.Distance;

public class PathFindResult {

    private final Distance distance;
    private final List<SectionEdge> path;

    public PathFindResult(final Distance distance, final List<SectionEdge> path) {
        this.distance = distance;
        this.path = path;
    }

    public int getDistanceValue() {
        return distance.getValue();
    }

    public List<SectionEdge> getPath() {
        return path;
    }
}
