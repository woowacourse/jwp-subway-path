package subway.domain.dijkstra;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Line;
import subway.domain.Station;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Dijkstra {

    protected final WeightedMultigraph<Station, DefaultWeightedEdge> graph =
            new WeightedMultigraph<>(DefaultWeightedEdge.class);

    protected final GraphPath<Station, DefaultWeightedEdge> shortestPath(
            final List<Station> stations,
            final List<Line> lines,
            final Station start,
            final Station end
    ) {
        for (final Station station : stations) {
            graph.addVertex(station);
        }

        addEdges(lines, graph);

        return new DijkstraShortestPath<>(graph).getPath(start, end);
    }

    private void addEdges(final List<Line> lines, final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lines.stream()
                .map(Line::getPaths)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .forEach(entry -> graph.setEdgeWeight(
                        graph.addEdge(entry.getKey(), entry.getValue().getNext()), entry.getValue().getDistance())
                );
    }
}
