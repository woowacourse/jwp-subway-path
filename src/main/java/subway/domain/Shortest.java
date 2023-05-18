package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.path.Path;
import subway.domain.path.PathEdgeProxy;
import subway.domain.path.Paths;

import java.util.List;
import java.util.stream.Collectors;

public final class Shortest {

    private final DijkstraShortestPath<Station, PathEdgeProxy> graph;

    public Shortest(final DijkstraShortestPath<Station, PathEdgeProxy> graph) {
        this.graph = graph;
    }

    public static Shortest from(final List<Paths> allPaths) {
        final WeightedMultigraph<Station, PathEdgeProxy> graph = new WeightedMultigraph<>(PathEdgeProxy.class);
        for (final Paths paths : allPaths) {
            paths.getStations().forEach(graph::addVertex);
            paths.toList()
                    .forEach(path -> graph.addEdge(path.getUp(), path.getDown(), PathEdgeProxy.from(path)));
        }

        return new Shortest(new DijkstraShortestPath<>(graph));
    }

    public Paths findShortest(final Station start, final Station end) {
        try {
            final List<PathEdgeProxy> result = graph.getPath(start, end).getEdgeList();

            final List<Path> paths = result.stream()
                    .map(PathEdgeProxy::toPath)
                    .collect(Collectors.toList());
            return new Paths(paths);
        } catch (final Exception e) {
            return new Paths();
        }
    }
}
