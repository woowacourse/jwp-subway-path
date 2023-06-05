package subway.route;

import java.util.List;
import subway.route.application.port.in.find.RouteFindRequestDto;
import subway.route.application.port.in.find.RouteFindResponseDto;
import subway.route.application.service.find.dto.RouteEdgeResponseDto;
import subway.route.find.dto.RouteEdgeResponse;
import subway.route.find.dto.RouteFindRequest;
import subway.route.find.dto.RouteFindResponse;

public class RouteAssembler {

    private RouteAssembler() {
    }

    public static RouteFindRequestDto toRouteFindRequestDto(RouteFindRequest request) {
        return new RouteFindRequestDto(request.getSourceId(), request.getTargetId());
    }

    public static RouteFindResponse toRouteFindResponse(RouteFindResponseDto responseDto) {
        return new RouteFindResponse(toRouteEdgeResponse(responseDto.getStations()), responseDto.getDistance(),
                responseDto.getFare());
    }

    private static List<RouteEdgeResponse> toRouteEdgeResponse(List<RouteEdgeResponseDto> stations) {
        return stations.stream()
                .map(RouteAssembler::toRouteEdgeResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    private static RouteEdgeResponse toRouteEdgeResponse(RouteEdgeResponseDto station) {
        return new RouteEdgeResponse(station.getSourceId(), station.getTargetId(), station.getDistance(),
                station.getLineId());
    }
}
