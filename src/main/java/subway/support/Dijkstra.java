package subway.support;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Dijkstra<T> {

    private final WeightedMultigraph<T, DefaultWeightedEdge> graph =
            new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public final GraphPath<T, DefaultWeightedEdge> shortestPath(
            final List<T> startValues,
            final List<T> nextValues,
            final List<Integer> distances,
            final T start,
            final T end
    ) {
        for (int i = 0; i < startValues.size(); i++) {
            graph.addVertex(startValues.get(i));
            graph.addVertex(nextValues.get(i));
        }
        addEdges(startValues, nextValues, distances);

        return new DijkstraShortestPath<>(graph).getPath(start, end);
    }

    private void addEdges(final List<T> values, final List<T> nextValues, final List<Integer> distances) {
        for (int i = 0; i < values.size(); i++) {
            graph.setEdgeWeight(graph.addEdge(values.get(i), nextValues.get(i)), distances.get(i));
        }
    }
}
