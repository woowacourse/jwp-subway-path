package subway.application.port;

import org.springframework.stereotype.Component;
import subway.application.core.domain.RouteMap;
import subway.application.core.domain.Station;

import java.util.List;

@Component
public interface PathFinder {

    List<Station> findShortestPath(List<RouteMap> routeMap);

    Integer calculateDistance(List<RouteMap> routeMap);
}
