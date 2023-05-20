package subway.domain.route;

import java.util.List;
import subway.domain.line.Distance;
import subway.domain.line.Station;

public class Subway {

    private final RouteGraph routeGraph;

    public Subway(RouteGraph routeGraph) {
        this.routeGraph = routeGraph;
    }

    public List<Station> findShortestRoute(Station start, Station end) {
        return routeGraph.findShortestRoute(start, end);
    }

    public Distance findShortestDistance(Station start, Station end) {
        return routeGraph.findShortestDistance(start, end);
    }
}
