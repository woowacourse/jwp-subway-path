package subway.application.port.in.route;

import subway.application.dto.RouteResponse;
import subway.adapter.in.web.route.dto.FindShortCutRequest;

public interface FindRouteResultUseCase {
    RouteResponse findResultShotCut(final FindShortCutRequest findShortCutRequest);
}
