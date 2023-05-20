package subway.domain.path;

import java.util.ArrayList;
import java.util.List;

public class PathSections {

    private final List<PathSection> pathSections;

    private PathSections(final List<PathSection> pathSections) {
        validateEmptyPathSections(pathSections);

        this.pathSections = pathSections;
    }

    private void validateEmptyPathSections(final List<PathSection> pathSections) {
        if (pathSections.isEmpty()) {
            throw new IllegalArgumentException("출발 역에서 도착 역까지 이동할 수 있는 경로가 없습니다.");
        }
    }

    public static PathSections from(final List<PathSection> pathSections) {
        return new PathSections(pathSections);
    }

    public int calculateSectionDistance() {
        return pathSections.stream()
                .mapToInt(PathSection::distance)
                .sum();
    }

    public List<PathSection> getPathSections() {
        return new ArrayList<>(pathSections);
    }
}
