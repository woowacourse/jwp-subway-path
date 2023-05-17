package subway.common.mapper;

import java.util.List;
import java.util.stream.Collectors;
import subway.application.port.in.route.dto.response.RouteQueryResponse;
import subway.application.port.in.station.dto.response.StationQueryResponse;
import subway.domain.Route;

public class RouteMapper {

    private RouteMapper() {
    }

    public static RouteQueryResponse toResponse(final Route route) {
        List<StationQueryResponse> stations = route.getRoute().stream()
                .map(StationMapper::toResponse)
                .collect(Collectors.toList());
        return new RouteQueryResponse(stations, route.getDistance());
    }
}
