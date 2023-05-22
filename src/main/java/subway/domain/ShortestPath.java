package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.graph.Graph;
import subway.exeption.InvalidPathException;

import java.util.List;
import java.util.stream.Collectors;

public class ShortestPath {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    public ShortestPath(final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public static ShortestPath from(final List<Sections> sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> mergedGraph = mergeGraph(sections);
        final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(mergedGraph);
        return new ShortestPath(shortestPath);
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> mergeGraph(final List<Sections> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> mergedGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        final List<Graph> graphs = sections.stream()
                .map(Sections::getGraph)
                .collect(Collectors.toList());

        for (final Graph graph : graphs) {
            Graphs.addGraph(mergedGraph, graph.getGraph());
        }

        return mergedGraph;
    }

    public List<Station> path(final Station source, final Station target) {
        final GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(source, target);
        if (path == null) {
            throw new InvalidPathException("연결되지 않은 역에 대해 경로를 조회할 수 없습니다.");
        }
        return path.getVertexList();
    }

    public double distance(final Station source, final Station target) {
        return shortestPath.getPathWeight(source, target);
    }
}
