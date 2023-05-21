package subway.application.port.in.route;

import subway.application.port.in.route.dto.command.FindRouteCommand;
import subway.application.port.in.route.dto.response.RouteQueryResponse;

public interface FindRouteUseCase {

    RouteQueryResponse findRoute(FindRouteCommand command);
}
