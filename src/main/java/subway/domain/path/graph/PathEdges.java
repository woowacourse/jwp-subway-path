package subway.domain.path.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import subway.domain.path.PathSection;
import subway.domain.path.PathSections;

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

    public int calculateTotalPathSectionDistance() {
        return pathEdges.stream()
                .mapToInt(PathEdge::getDistance)
                .sum();
    }

    public PathSections to() {
        final List<PathSection> pathSections = pathEdges.stream()
                .map(PathEdge::to)
                .collect(Collectors.toList());

        return PathSections.from(pathSections);
    }

    public boolean isSameLine(final PathEdge targetPathEdge) {
        if (pathEdges.isEmpty()) {
            return true;
        }
        return pathEdges.stream()
                .anyMatch(sourcePathEdge -> sourcePathEdge.isSameLine(targetPathEdge));
    }

    public List<PathEdge> getPathSections() {
        return pathEdges;
    }
}
