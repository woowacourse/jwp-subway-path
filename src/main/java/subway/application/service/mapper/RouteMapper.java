package subway.application.service.mapper;

import java.util.List;
import java.util.stream.Collectors;
import subway.application.port.in.route.dto.response.RouteQueryResponse;
import subway.application.port.in.station.dto.response.StationQueryResponse;
import subway.domain.fare.Fare;
import subway.domain.route.Route;

public class RouteMapper {

    private RouteMapper() {
    }

    public static RouteQueryResponse toResponse(final Route route, final Fare fare) {
        List<StationQueryResponse> stations = route.findStationRoute().stream()
                .map(StationMapper::toResponse)
                .collect(Collectors.toList());
        return new RouteQueryResponse(stations, route.findTotalDistance(), fare.getValue());
    }
}
