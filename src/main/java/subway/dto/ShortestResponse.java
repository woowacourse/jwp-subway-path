package subway.dto;

import subway.domain.fare.FareInfo;

import java.util.List;
import java.util.stream.Collectors;

public final class ShortestResponse {
    private final int totalDistance;
    private final int totalCost;
    private final List<PathResponse> paths;

    public ShortestResponse(final int totalDistance, final int totalCost, final List<PathResponse> paths) {
        this.totalDistance = totalDistance;
        this.totalCost = totalCost;
        this.paths = paths;
    }

    public static ShortestResponse of(final FareInfo fareInfo) {
        final List<PathResponse> pathResponses = fareInfo.getShortest().stream()
                .map(PathResponse::from)
                .collect(Collectors.toUnmodifiableList());

        return new ShortestResponse(fareInfo.getDistance(), fareInfo.getFare(), pathResponses);
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public List<PathResponse> getPaths() {
        return paths;
    }
}
