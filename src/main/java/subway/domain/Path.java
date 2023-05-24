package subway.domain;

import java.util.List;

public class Path {
    private final List<PathSegment> pathSegments;

    public Path(List<PathSegment> pathSegments) {
        this.pathSegments = pathSegments;
    }

    public Fare calculateTotalFare(FareCalculator calculator) {
        return calculator.calculate(calculateTotalDistance());
    }

    public List<PathSegment> getPathSegments() {
        return pathSegments;
    }

    public Distance calculateTotalDistance() {
        return  pathSegments.stream()
                .map(PathSegment::calculateDistance)
                .reduce(Distance.from(0), Distance::plus);
    }
}
