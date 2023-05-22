package subway.infrastructure.graph;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.application.core.domain.RouteMap;
import subway.application.core.domain.Section;
import subway.application.core.domain.Station;
import subway.application.core.service.dto.out.PathFindResult;
import subway.application.port.PathFinder;

import java.util.List;

@Component
public class JgraphtPathFinder implements PathFinder {

    @Override
    public PathFindResult findShortestPath(List<RouteMap> routeMap, Station departure, Station terminal) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = setupGraph(routeMap);
        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(departure, terminal);
        return new PathFindResult(path.getVertexList(), path.getWeight());
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> setupGraph(List<RouteMap> routeMap) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        routeMap.stream()
                .map(RouteMap::values)
                .forEach(stations -> applyRouteToGraph(graph, stations));
        return graph;
    }

    private void applyRouteToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        sections.forEach(section -> {
            Station upBound = section.getUpBound();
            Station downBound = section.getDownBound();
            graph.addVertex(upBound);
            graph.addVertex(downBound);
            graph.setEdgeWeight(graph.addEdge(upBound, downBound), section.getDistance().value());
        });
    }
}
