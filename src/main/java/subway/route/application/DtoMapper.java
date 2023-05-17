package subway.route.application;

import subway.route.application.dto.RouteDto;
import subway.route.domain.RouteSegment;

import java.util.List;

class DtoMapper {

    private DtoMapper() {
    }

    public static RouteDto toRouteDto(List<RouteSegment> route, int fare) {
        return new RouteDto(route, fare);
    }
}
