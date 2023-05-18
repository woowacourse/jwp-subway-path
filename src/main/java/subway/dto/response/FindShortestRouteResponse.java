package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Path;
import subway.dto.LineInPathDto;

public class FindShortestRouteResponse {
    private final List<LineInPathDto> path;
    private final Double totalDistance;
    private final Double totalCharge;

    public FindShortestRouteResponse(List<LineInPathDto> path, Double totalDistance, Double totalCharge) {
        this.path = path;
        this.totalDistance = totalDistance;
        this.totalCharge = totalCharge;
    }

    public static FindShortestRouteResponse from(Path path) {
        List<LineInPathDto> lines = path.getLines().stream()
                .map(it -> LineInPathDto.from(it))
                .collect(Collectors.toList());
        return new FindShortestRouteResponse(lines, path.getTotalDistance(), path.getTotalCharge());
    }

    public List<LineInPathDto> getPath() {
        return path;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public Double getTotalCharge() {
        return totalCharge;
    }
}
