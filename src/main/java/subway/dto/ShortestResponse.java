package subway.dto;

import java.util.List;

public final class ShortestResponse {
    private final Long totalDistance;
    private final List<PathResponse> paths;

    public ShortestResponse(final Long totalDistance, final List<PathResponse> paths) {
        this.totalDistance = totalDistance;
        this.paths = paths;
    }

    public Long getTotalDistance() {
        return totalDistance;
    }

    public List<PathResponse> getPaths() {
        return paths;
    }
}
