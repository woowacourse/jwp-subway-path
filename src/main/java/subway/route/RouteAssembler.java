package subway.route;

import java.util.List;
import subway.route.dto.request.RouteFindRequest;
import subway.route.dto.request.RouteFindRequestDto;
import subway.route.dto.response.RouteEdgeResponse;
import subway.route.dto.response.RouteEdgeResponseDto;
import subway.route.dto.response.RouteFindResponse;
import subway.route.dto.response.RouteFindResponseDto;

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
