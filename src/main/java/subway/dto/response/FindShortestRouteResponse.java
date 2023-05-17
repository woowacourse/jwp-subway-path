package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Path;

public class FindShortestRouteResponse {
    private final List<StationInfoResponse> shortestRoute;
    private final Double totalDistance;
    private final Double totalCharge;

    public FindShortestRouteResponse(List<StationInfoResponse> shortestRoute, Double totalDistance,
                                     Double totalCharge) {
        this.shortestRoute = shortestRoute;
        this.totalDistance = totalDistance;
        this.totalCharge = totalCharge;
    }

    public static FindShortestRouteResponse from(Path path) {
        List<StationInfoResponse> stationInfos = path.getStations()
                .stream()
                .map(station -> new StationInfoResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
        return new FindShortestRouteResponse(stationInfos, path.getTotalDistance(), path.getTotalCharge());
    }

    public List<StationInfoResponse> getShortestRoute() {
        return shortestRoute;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public Double getTotalCharge() {
        return totalCharge;
    }
}
