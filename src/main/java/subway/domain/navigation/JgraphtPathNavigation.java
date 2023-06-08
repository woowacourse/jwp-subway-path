package subway.domain.navigation;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.line.edge.StationEdge;

import java.util.Collection;
import java.util.List;

@Component
public class JgraphtPathNavigation implements PathNavigation {

    @Override
    public List<Long> findPath(final Long startStationId, final Long endStationId, final Collection<StationEdge> subwayGraph) {
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        setGraph(subwayGraph, graph);

        final DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        return dijkstraShortestPath.getPath(startStationId, endStationId).getVertexList();
    }

    private void setGraph(final Collection<StationEdge> subwayGraph, final WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (StationEdge stationEdge : subwayGraph) {
            final Long upStationId = stationEdge.getUpStationId();
            final Long downStationId = stationEdge.getDownStationId();
            graph.addVertex(upStationId);
            graph.addVertex(downStationId);
            graph.setEdgeWeight(graph.addEdge(upStationId, downStationId), stationEdge.getDistance());
        }
    }
}
