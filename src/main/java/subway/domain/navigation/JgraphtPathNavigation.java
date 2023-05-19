package subway.domain.navigation;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.line.edge.StationEdge;
import subway.domain.line.edge.StationEdges;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JgraphtPathNavigation implements PathNavigation {

    @Override
    public StationEdges findPath(final Long startStationId, final Long endStationId, final Set<StationEdge> subwayGraph) {
        final WeightedMultigraph<Long, StationGraphEdge> graph = new WeightedMultigraph<>(StationGraphEdge.class);
        setGraph(subwayGraph, graph);

        final DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        final List<StationGraphEdge> graphPathEdges = dijkstraShortestPath.getPath(startStationId, endStationId).getEdgeList();

        final List<StationEdge> stationEdges = graphPathEdges.stream()
                .map(StationGraphEdge::toStationEdge)
                .collect(Collectors.toUnmodifiableList());

        return new StationEdges(stationEdges);
    }

    private void setGraph(final Set<StationEdge> subwayGraph, final WeightedMultigraph<Long, StationGraphEdge> graph) {
        for (StationEdge stationEdge : subwayGraph) {
            final Long upStationId = stationEdge.getUpStationId();
            final Long downStationId = stationEdge.getDownStationId();
            graph.addVertex(upStationId);
            graph.addVertex(downStationId);
            graph.setEdgeWeight(graph.addEdge(upStationId, downStationId), stationEdge.getDistance());
        }
    }

    private static class StationGraphEdge extends DefaultWeightedEdge {

        private static final long serialVersionUID = 1L;

        public StationEdge toStationEdge() {
            return new StationEdge((Long) getSource(), (Long) getTarget(), (int) getWeight());
        }
    }
}
