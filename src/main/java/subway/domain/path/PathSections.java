package subway.domain.path;

import java.util.ArrayList;
import java.util.List;

public class PathSections {

    private final List<PathSection> pathSections;

    public PathSections(final List<PathSection> pathSections) {
        this.pathSections = pathSections;
    }

    public static PathSections create() {
        return new PathSections(new ArrayList<>());
    }

    public void add(final PathSection pathSection) {
        pathSections.add(pathSection);
    }

    public boolean isOtherLine(final PathSection targetPathSection) {
        return pathSections.stream()
                .noneMatch(sourcePathSection -> sourcePathSection.isSameLine(targetPathSection));
    }

    public int calculateTotalPathSectionDistance() {
        return pathSections.stream()
                .mapToInt(PathSection::getDistance)
                .sum();
    }

    public List<PathSection> getPathSections() {
        return pathSections;
    }
}
