package subway.route.dto.response;

import java.util.List;

public class RouteFindResponseDto {

    private final List<RouteEdgeResponseDto> stations;
    private final long distance;
    private final long fare;

    public RouteFindResponseDto(List<RouteEdgeResponseDto> stations, long distance, long fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<RouteEdgeResponseDto> getStations() {
        return stations;
    }

    public long getDistance() {
        return distance;
    }

    public long getFare() {
        return fare;
    }
}
