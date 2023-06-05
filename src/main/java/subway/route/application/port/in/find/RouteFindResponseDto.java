package subway.route.application.port.in.find;

import java.util.List;
import subway.route.application.service.find.dto.RouteEdgeResponseDto;

public class RouteFindResponseDto {

    private final List<RouteEdgeResponseDto> stations;
    private final long distance;
    private final long fare;

    public RouteFindResponseDto(final List<RouteEdgeResponseDto> stations, final long distance, final long fare) {
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
