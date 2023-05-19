package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.exception.NoSuchRouteException;
import subway.repository.LineRepository;

import java.util.List;
import java.util.Map;

@Component
public class ShortestRouteFinder implements RouteFinder {

    private final LineRepository lineRepository;

    public ShortestRouteFinder(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<Station> findRoute(final Station from, final Station to) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> path = createPath(lineRepository.findAll());
        try {
            return path.getPath(from, to).getVertexList();
        } catch (IllegalArgumentException e) {
            throw new NoSuchRouteException(from, to);
        }
    }

    public int getDistance(final Station from, final Station to) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = createPath(lineRepository.findAll());
        try {
            return (int) dijkstraShortestPath.getPath(from, to).getWeight();
        } catch (IllegalArgumentException e) {
            throw new NoSuchRouteException(from, to);
        }
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createPath(final List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            addVertex(graph, line);
            addEdges(graph, line);
        }
        return new DijkstraShortestPath<>(graph);
    }

    private void addEdges(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final Line line) {
        for (Section section : line.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private void addVertex(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final Line line) {
        Map<Station, Station> sectionsByMap = line.getSectionsByMap();
        for (Station station : sectionsByMap.keySet()) {
            graph.addVertex(station);
        }
        for (Station station : sectionsByMap.values()) {
            graph.addVertex(station);
        }
    }
}
