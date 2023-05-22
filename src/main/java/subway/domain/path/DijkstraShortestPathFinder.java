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

public class DijkstraShortestPathFinder implements PathFinder {

    DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    public DijkstraShortestPathFinder(final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public DijkstraShortestPathFinder() {
        this(null);
    }

    @Override
    public PathFinder registerSections(final List<Sections> sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> mergedGraph = mergeGraph(sections);
        final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(mergedGraph);
        return new DijkstraShortestPathFinder(shortestPath);
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
    public double findDistance(final Station source, final Station target) {
        validateNullPath();
        return shortestPath.getPathWeight(source, target);
    }

    @Override
    public List<Station> findPath(final Station source, final Station target) {
        validateNullPath();
        final GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(source, target);
        if (path == null) {
            throw new InvalidPathException("연결되지 않은 역에 대해 경로를 조회할 수 없습니다.");
        }
        return path.getVertexList();
    }

    private void validateNullPath() {
        if (shortestPath == null) {
            throw new InvalidPathException("노선 정보를 먼저 등록해 주세요.");
        }
    }
}
