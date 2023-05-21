package subway.dto;

import subway.domain.path.PathEdgeProxy;

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

    public static ShortestResponse of(final List<PathEdgeProxy> paths, final int totalCost) {
        final long distance = paths.stream()
                .mapToLong(PathEdgeProxy::getDistance)
                .sum();

        final List<PathResponse> pathResponses = paths.stream()
                .map(PathResponse::from)
                .collect(Collectors.toUnmodifiableList());

        return new ShortestResponse(distance, totalCost, pathResponses);
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
