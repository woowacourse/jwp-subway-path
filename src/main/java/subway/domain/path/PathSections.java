package subway.domain.path;

import java.util.ArrayList;
import java.util.List;

public class PathSections {

    private final List<PathEdge> pathEdges;

    public PathSections(final List<PathEdge> pathEdges) {
        this.pathEdges = pathEdges;
    }

    public static PathSections create() {
        return new PathSections(new ArrayList<>());
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
