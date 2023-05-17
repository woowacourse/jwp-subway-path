package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.ShortestRouteWithStation;

public class FindShortestRouteResponse {
    private final List<StationInfoResponse> shortestRoute;
    private final Double totalDistance;
    private final Integer totalCharge;

    public FindShortestRouteResponse(List<StationInfoResponse> shortestRoute, Double totalDistance, Integer totalCharge) {
        this.shortestRoute = shortestRoute;
        this.totalDistance = totalDistance;
        this.totalCharge = totalCharge;
    }

    public static FindShortestRouteResponse from(ShortestRouteWithStation shortestRoute) {
        List<StationInfoResponse> stationInfos = shortestRoute.getStations()
                .stream()
                .map(station -> new StationInfoResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
        return new FindShortestRouteResponse(stationInfos, shortestRoute.getTotalDistance(),
                shortestRoute.getTotalFare());
    }

    public List<StationInfoResponse> getShortestRoute() {
        return shortestRoute;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public Integer getTotalCharge() {
        return totalCharge;
    }
}
