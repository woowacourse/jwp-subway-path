package subway.dto;

import subway.domain.PathEdge;

import java.util.List;
import java.util.stream.Collectors;

public final class ShortestResponse {
    private final Long totalDistance;
    private final List<PathResponse> paths;

    public ShortestResponse(final Long totalDistance, final List<PathResponse> paths) {
        this.totalDistance = totalDistance;
        this.paths = paths;
    }

    public static ShortestResponse from(final List<PathEdge> paths) {
        final long totalDistance = calculateTotalDistance(paths);
        final List<PathResponse> pathResponses = toPathResponses(paths);

        return new ShortestResponse(totalDistance, pathResponses);
    }

    private static long calculateTotalDistance(final List<PathEdge> paths) {
        return paths.stream()
                .mapToLong(PathEdge::getDistance)
                .sum();
    }

    private static List<PathResponse> toPathResponses(final List<PathEdge> paths) {
        return paths.stream()
                .map(PathResponse::from)
                .collect(Collectors.toList());
    }

    public Long getTotalDistance() {
        return totalDistance;
    }

    public List<PathResponse> getPaths() {
        return paths;
    }
}
