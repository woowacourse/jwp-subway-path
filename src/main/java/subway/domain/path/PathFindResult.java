package subway.domain.path;

import static java.util.stream.Collectors.toList;

import java.util.List;
import subway.domain.core.Distance;
import subway.domain.core.Section;

public class PathFindResult {

    private final Distance distance;
    private final List<SectionEdge> path;

    public PathFindResult(final Distance distance, final List<SectionEdge> path) {
        this.distance = distance;
        this.path = path;
    }

    public List<Section> toSections() {
        return path.stream()
                .map(SectionEdge::toSection)
                .collect(toList());
    }

    public Distance getDistance() {
        return distance;
    }

    public List<SectionEdge> getPath() {
        return path;
    }
}
