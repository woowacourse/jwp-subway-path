package subway.domain.path;

import java.util.ArrayList;
import java.util.List;

public class PathEdges {

    private final List<PathEdge> pathEdges;

    public PathEdges(final List<PathEdge> pathEdges) {
        this.pathEdges = pathEdges;
    }

    public static PathEdges create() {
        return new PathEdges(new ArrayList<>());
    }

    public void add(final PathEdge pathEdge) {
        this.pathEdges.add(pathEdge);
    }

    public boolean isOtherLine(final PathEdge targetPathEdge) {
        return pathEdges.stream()
                .noneMatch(sourcePathSection -> sourcePathSection.isSameLine(targetPathEdge));
    }

    public int calculateTotalPathSectionDistance() {
        return pathEdges.stream()
                .mapToInt(PathEdge::getDistance)
                .sum();
    }

    public List<PathEdge> getPathSections() {
        return pathEdges;
    }
}
