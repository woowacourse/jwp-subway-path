package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.path.PathEdgeProxy;
import subway.domain.path.Paths;

import java.util.Collections;
import java.util.List;

public final class Shortest {

    private final DijkstraShortestPath<Station, PathEdgeProxy> graph;

    public Shortest(final DijkstraShortestPath<Station, PathEdgeProxy> graph) {
        this.graph = graph;
    }

    public static Shortest from(final List<Line> allLines) {
        final WeightedMultigraph<Station, PathEdgeProxy> graph = new WeightedMultigraph<>(PathEdgeProxy.class);
        for (final Line line : allLines) {
            final Paths paths = line.getPaths();
            paths.getStations().forEach(graph::addVertex);
            paths.toList()
                    .forEach(path -> {
                        final PathEdgeProxy edge = new PathEdgeProxy(path, line.getAdditionalFare());
                        graph.addEdge(path.getUp(), path.getDown(), edge);
                    });
        }

        return new Shortest(new DijkstraShortestPath<>(graph));
    }

    public List<PathEdgeProxy> findShortest(final Station start, final Station end) {
        try {
            return graph.getPath(start, end).getEdgeList();
        } catch (final NullPointerException | IllegalArgumentException e) {
            return Collections.emptyList();
        }
    }
}
