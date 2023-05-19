package subway.dto;

import subway.domain.FareStrategy;
import subway.domain.path.Paths;

import java.util.List;
import java.util.stream.Collectors;

public final class ShortestResponse {
    private final Long totalDistance;
    private final int totalCost;
    private final List<PathResponse> paths;

    public ShortestResponse(final Long totalDistance, final int totalCost, final List<PathResponse> paths) {
        this.totalDistance = totalDistance;
        this.totalCost = totalCost;
        this.paths = paths;
    }

    public static ShortestResponse of(final Paths paths, final FareStrategy fareStrategy) {
        final long distance = paths.getTotalDistance();
        final int fare = fareStrategy.calculate(distance);
        final List<PathResponse> pathResponses = paths.toList().stream()
                .map(PathResponse::from)
                .collect(Collectors.toUnmodifiableList());

        return new ShortestResponse(distance, fare, pathResponses);
    }

    public Long getTotalDistance() {
        return totalDistance;
    }

    public List<PathResponse> getPaths() {
        return paths;
    }

    public int getTotalCost() {
        return totalCost;
    }
}
