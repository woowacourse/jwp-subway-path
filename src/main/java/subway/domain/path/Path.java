package subway.domain.path;

import java.util.ArrayList;
import java.util.List;

public class Path {

    private final List<PathSections> pathSections;

    private Path(final List<PathSections> pathSections) {
        validatePathSections(pathSections);

        this.pathSections = pathSections;
    }

    private void validatePathSections(final List<PathSections> pathSections) {
        if (pathSections.isEmpty()) {
            throw new IllegalArgumentException("출발 역에서 도착 역까지 이동할 수 있는 경로가 없습니다.");
        }
    }

    public static Path from(final List<PathSections> paths) {
        return new Path(paths);
    }

    public int calculateTotalDistance() {
        return pathSections.stream()
                .mapToInt(PathSections::calculateSectionDistance)
                .sum();
    }

    public List<PathSections> getPathSections() {
        return new ArrayList<>(pathSections);
    }
}
