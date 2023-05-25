package subway.route.domain.jgraph;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import subway.route.domain.RouteFinder;
import subway.route.domain.RouteSegment;
import subway.route.exception.IdenticalStationsException;
import subway.route.exception.RouteNotFoundException;
import subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JgraphRouteFinder implements RouteFinder<RouteSegment> {

    private final DijkstraShortestPath<Station, WeightedEdgeWithLineMetadata> shortestPath;

    public JgraphRouteFinder(DijkstraShortestPath<Station, WeightedEdgeWithLineMetadata> shortestPath) {
        this.shortestPath = shortestPath;
    }

    @Override
    public List<RouteSegment> getRoute(Station source, Station destination) {
        validateNotIdenticalStations(source, destination);

        final List<RouteSegment> route = new ArrayList<>();
        final GraphPath<Station, WeightedEdgeWithLineMetadata> path = shortestPath.getPath(source, destination);
        validatePathExist(source, destination, path);

        final List<Station> stationsAlongPath = path.getVertexList();
        final List<WeightedEdgeWithLineMetadata> sectionsAlongPath = path.getEdgeList();

        addRouteSegment(route, stationsAlongPath, sectionsAlongPath);

        return route;
    }

    private void validateNotIdenticalStations(Station source, Station destination) {
        if (source.equals(destination)) {
            throw new IdenticalStationsException("시작역과 도착역이 동일합니다 source id: " + source.getId() + " destination id: " + destination.getId());
        }
    }

    private void validatePathExist(Station source, Station destination, GraphPath<Station, WeightedEdgeWithLineMetadata> path) {
        if (Objects.isNull(path)) {
            throw new RouteNotFoundException(source.getId(), destination.getId());
        }
    }

    private void addRouteSegment(List<RouteSegment> route, List<Station> stationsOfPath, List<WeightedEdgeWithLineMetadata> sectionsOfPath) {
        for (int i = 0; i < sectionsOfPath.size(); i++) {
            final Station upstream = stationsOfPath.get(i);
            final Station downstream = stationsOfPath.get(i + 1);
            final WeightedEdgeWithLineMetadata section = sectionsOfPath.get(i);
            final RouteSegment routeSegment = new RouteSegment(
                    upstream.getId(),
                    upstream.getName(),
                    downstream.getId(),
                    downstream.getName(),
                    section.getLindId(),
                    section.getLineInfo(),
                    section.getDistance()
            );

            route.add(routeSegment);
        }
    }

    @Override
    public int getTotalWeight(Station source, Station destination) {
        return (int) shortestPath.getPathWeight(source, destination);
    }
}
