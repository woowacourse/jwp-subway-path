package subway.dto;

import java.util.List;
import subway.domain.TransferableRoute;

public class RouteResponse {

    private final List<StationResponse> stations;
    private final int totalDistance;
    private final int totalFare;

    private RouteResponse(final List<StationResponse> stations, final int totalDistance, final int totalFare) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.totalFare = totalFare;
    }

    public static RouteResponse from(final TransferableRoute route) {
        return new RouteResponse(
                StationResponse.from(route.stations()),
                route.totalDistance().getValue(),
                route.totalFare()
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
