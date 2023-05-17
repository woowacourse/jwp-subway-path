package subway.domain;

import java.util.List;

public class Path {
    private final List<LineSegment> lineSegments;

    public Path(List<LineSegment> lineSegments) {
        this.lineSegments = lineSegments;
    }

    public List<LineSegment> getLineSegments() {
        return lineSegments;
    }

    public Distance calculateTotalDistance() {
        return  lineSegments.stream()
                .map(LineSegment::calculateDistance)
                .reduce(Distance.from(0), Distance::plus);
    }
}
