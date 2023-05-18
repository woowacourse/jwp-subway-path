package subway.ui.route;

import subway.application.route.port.in.find.RouteFindRequestDto;
import subway.application.route.port.in.find.RouteFindResponseDto;
import subway.ui.route.find.dto.RouteFindRequest;
import subway.ui.route.find.dto.RouteFindResponse;

public class RouteAssembler {

    private RouteAssembler() {
    }

    public static RouteFindRequestDto toRouteFindRequestDto(final RouteFindRequest request) {
        return new RouteFindRequestDto(request.getSourceId(), request.getTargetId());
    }

    public static RouteFindResponse toRouteFindResponse(final RouteFindResponseDto responseDto) {
        return new RouteFindResponse(responseDto.getStations(), responseDto.getDistance(), responseDto.getDistance());
    }
}
