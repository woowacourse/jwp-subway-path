package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Path;
import subway.dto.RouteDto;

public class FindShortestRouteResponse {
    private final List<RouteDto> path;
    private final Double totalDistance;
    private final Double totalCharge;

    public FindShortestRouteResponse(List<RouteDto> path, Double totalDistance, Double totalCharge) {
        this.path = path;
        this.totalDistance = totalDistance;
        this.totalCharge = totalCharge;
    }

    public static FindShortestRouteResponse from(Path path) {
        List<RouteDto> lines = path.getRoutes().stream()
                .map(it -> RouteDto.from(it))
                .collect(Collectors.toList());
        return new FindShortestRouteResponse(lines, path.getTotalDistance(), path.getTotalCharge().getValue());
    }

    public List<RouteDto> getPath() {
        return path;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public Double getTotalCharge() {
        return totalCharge;
    }
}
