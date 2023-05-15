package subway.domain.graph;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.edge.Edge;
import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.List;

public class SubwayGraph {

    private SubwayGraph() {
    }

    public static DijkstraShortestPath<Station, Edge> getShortestPath(final List<Line> lines) {
        final WeightedMultigraph<Station, Edge> graph = new WeightedMultigraph<>(Edge.class);
        for (final Line line : lines) {
            line.stations().forEach(v -> {
                if (!graph.containsVertex(v)) {
                    graph.addVertex(v);
                }
            });
            line.edges()
                    .forEach(m -> {
                        final Edge edge = graph.addEdge(m.getUpStation(), m.getDownStation());
                        graph.setEdgeWeight(edge, m.getDistanceValue());
                    });
        }

        return new DijkstraShortestPath<>(graph);
    }
}
