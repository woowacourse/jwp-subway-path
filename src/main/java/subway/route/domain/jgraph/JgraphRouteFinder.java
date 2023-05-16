package subway.route.domain.jgraph;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import subway.route.domain.RouteFinder;
import subway.route.domain.RouteSegment;
import subway.station.domain.Station;

import java.util.List;

public class JgraphRouteFinder implements RouteFinder<RouteSegment> {

    private final DijkstraShortestPath<Station, WeightedEdgeWithLineInfo> shortestPath;

    public JgraphRouteFinder(DijkstraShortestPath<Station, WeightedEdgeWithLineInfo> shortestPath) {
        this.shortestPath = shortestPath;
    }

    @Override
    public List<RouteSegment> getRoute(Station source, Station destination) {
        return null;
    }

    @Override
    public int getTotalWeight(Station source, Station destination) {
        return 0;
    }
}
