package subway.domain.graph;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.List;

public class SubwayGraph {

    private SubwayGraph() {
    }

    public static DijkstraShortestPath<Station, DefaultWeightedEdge> getShortestPath(final List<Line> lines) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (final Line line : lines) {
            line.stations().forEach(station -> {
                if (!graph.containsVertex(station)) {
                    graph.addVertex(station);
                }
            });
            line.sections()
                    .forEach(section -> {
                        final DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
                        graph.setEdgeWeight(edge, section.getDistanceValue());
                    });
        }

        return new DijkstraShortestPath<>(graph);
    }
}
