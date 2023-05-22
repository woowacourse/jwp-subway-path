package subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.graph.Graph;
import subway.exeption.InvalidPathException;

import java.util.List;
import java.util.stream.Collectors;

public class ShortestPath implements Path {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    private ShortestPath(final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public ShortestPath() {
        this(null);
    }

    @Override
    public ShortestPath registerSections(final List<Sections> sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> mergedGraph = mergeGraph(sections);
        final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(mergedGraph);
        return new ShortestPath(shortestPath);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> mergeGraph(final List<Sections> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> mergedGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        final List<Graph> graphs = sections.stream()
                .map(Sections::getGraph)
                .collect(Collectors.toList());

        for (final Graph graph : graphs) {
            Graphs.addGraph(mergedGraph, graph.getGraph());
        }

        return mergedGraph;
    }

    @Override
    public List<Station> path(final Station source, final Station target) {
        validateShortestPath();
        final GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(source, target);
        if (path == null) {
            throw new InvalidPathException("연결되지 않은 역에 대해 경로를 조회할 수 없습니다.");
        }
        return path.getVertexList();
    }

    @Override
    public double distance(final Station source, final Station target) {
        validateShortestPath();
        return shortestPath.getPathWeight(source, target);
    }

    private void validateShortestPath() {
        if (shortestPath == null) {
            throw new InvalidPathException("Section 정보를 먼저 입력해 주세요.");
        }
    }
}
