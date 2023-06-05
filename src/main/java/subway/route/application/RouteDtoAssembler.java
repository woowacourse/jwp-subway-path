package subway.route.application;

import java.util.List;
import java.util.stream.Collectors;
import subway.route.application.service.find.dto.RouteEdgeResponseDto;
import subway.route.domain.InterStationEdge;

public class RouteDtoAssembler {

    private RouteDtoAssembler() {
    }

    public static List<RouteEdgeResponseDto> toRouteEdgeResponseDto(List<InterStationEdge> edges) {
        return edges.stream()
                .map(RouteDtoAssembler::toRouteEdgeResponseDto)
                .collect(Collectors.toList());
    }

    private static RouteEdgeResponseDto toRouteEdgeResponseDto(InterStationEdge edge) {
        return new RouteEdgeResponseDto(edge.getUpStationId(), edge.getDownStationId(), edge.getWeight(),
                edge.getLineId());
    }
}
