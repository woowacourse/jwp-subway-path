package subway.dto;

import java.util.List;
import subway.domain.RouteInfo;

public class RouteResponse {

    private final List<StationResponse> stations;
    private final int totalDistance;
    private final int totalFare;

    private RouteResponse(final List<StationResponse> stations, final int totalDistance, final int totalFare) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.totalFare = totalFare;
    }

    public static RouteResponse from(final RouteInfo routeInfo) {
        return new RouteResponse(
                StationResponse.from(routeInfo.getStations()),
                routeInfo.getTotalDistance().getValue(),
                routeInfo.getTotalFare().getValue()
        );
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getTotalFare() {
        return totalFare;
    }
}
