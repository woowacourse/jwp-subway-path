package subway.application.port;

import org.springframework.stereotype.Component;
import subway.application.core.domain.RouteMap;
import subway.application.core.domain.Station;
import subway.application.core.service.dto.out.PathFindResult;

import java.util.List;

public interface PathFinder {

    PathFindResult findShortestPath(List<RouteMap> routeMap, Station departure, Station terminal);
}
