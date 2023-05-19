package subway.application.route;

import java.util.List;
import java.util.stream.Collectors;
import subway.application.route.service.find.dto.RouteEdgeResponseDto;
import subway.domain.route.InterStationEdge;

public class RouteDtoAssembler {

    private RouteDtoAssembler() {
    }

    public static List<RouteEdgeResponseDto> toRouteEdgeResponseDto(final List<InterStationEdge> edges) {
        return edges.stream()
                .map(RouteDtoAssembler::toRouteEdgeResponseDto)
                .collect(Collectors.toList());
    }

    private static RouteEdgeResponseDto toRouteEdgeResponseDto(final InterStationEdge edge) {
        return new RouteEdgeResponseDto(edge.getUpStationId(), edge.getDownStationId(), edge.getWeight(),
                edge.getLineId());
    }
}
