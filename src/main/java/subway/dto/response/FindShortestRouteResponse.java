package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.subway.Path;
import subway.dto.RouteDto;

public class FindShortestRouteResponse {
    private final List<RouteDto> path;
    private final double totalDistance;
    private final double totalCharge;

    public FindShortestRouteResponse(List<RouteDto> path, double totalDistance, double totalCharge) {
        this.path = path;
        this.totalDistance = totalDistance;
        this.totalCharge = totalCharge;
    }

    public static FindShortestRouteResponse from(Path path) {
        List<RouteDto> lines = path.getRoutes().stream()
                .map(it -> RouteDto.from(it))
                .collect(Collectors.toList());
        return new FindShortestRouteResponse(lines, path.getTotalDistance().getValue(), path.getTotalCharge().getValue());
    }

    public List<RouteDto> getPath() {
        return path;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public double getTotalCharge() {
        return totalCharge;
    }
}
