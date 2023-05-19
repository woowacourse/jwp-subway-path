package subway.ui.route;

import java.util.List;
import subway.application.route.port.in.find.RouteFindRequestDto;
import subway.application.route.port.in.find.RouteFindResponseDto;
import subway.application.route.service.find.dto.RouteEdgeResponseDto;
import subway.ui.route.find.dto.RouteEdgeResponse;
import subway.ui.route.find.dto.RouteFindRequest;
import subway.ui.route.find.dto.RouteFindResponse;

public class RouteAssembler {

    private RouteAssembler() {
    }

    public static RouteFindRequestDto toRouteFindRequestDto(final RouteFindRequest request) {
        return new RouteFindRequestDto(request.getSourceId(), request.getTargetId());
    }

    public static RouteFindResponse toRouteFindResponse(final RouteFindResponseDto responseDto) {
        return new RouteFindResponse(toRouteEdgeResponse(responseDto.getStations()), responseDto.getDistance(),
                responseDto.getFare());
    }

    private static List<RouteEdgeResponse> toRouteEdgeResponse(final List<RouteEdgeResponseDto> stations) {
        return stations.stream()
                .map(RouteAssembler::toRouteEdgeResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    private static RouteEdgeResponse toRouteEdgeResponse(final RouteEdgeResponseDto station) {
        return new RouteEdgeResponse(station.getSourceId(), station.getTargetId(), station.getDistance(),
                station.getLineId());
    }
}
