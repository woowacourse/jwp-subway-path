package subway.domain.graph;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.edge.Edge;
import subway.domain.edge.Edges;
import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.Map;

public class SubwayGraph {

    private SubwayGraph() {
    }

    public static DijkstraShortestPath<Station, Edge> getShortestPath(final Map<Line, Edges> allEdges) {
        final WeightedMultigraph<Station, Edge> graph = new WeightedMultigraph<>(Edge.class);
        for (Map.Entry<Line, Edges> entry : allEdges.entrySet()) {
            final Edges edges = entry.getValue();

            edges.getStations().forEach(v -> {
                if (!graph.containsVertex(v)) {
                    graph.addVertex(v);
                }
            });
            edges.getEdges()
                    .forEach(m -> {
                        final Edge edge = graph.addEdge(m.getUpStation(), m.getDownStation());
                        graph.setEdgeWeight(edge, m.getDistanceValue());
                    });
        }

        return new DijkstraShortestPath<>(graph);
    }
}
