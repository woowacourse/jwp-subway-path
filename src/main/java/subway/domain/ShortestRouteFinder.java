package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.exception.ApiNoSuchResourceException;
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
        DijkstraShortestPath<Station, StationEdge> path = createPath(lineRepository.findAll());
        try {
            return path.getPath(from, to).getVertexList();
        } catch (IllegalArgumentException e) {
            throw new NoSuchRouteException(from, to);
        }
    }

    public int getDistance(final Station from, final Station to) {
        DijkstraShortestPath<Station, StationEdge> dijkstraShortestPath = createPath(lineRepository.findAll());
        try {
            return (int) dijkstraShortestPath.getPath(from, to).getWeight();
        } catch (IllegalArgumentException e) {
            throw new NoSuchRouteException(from, to);
        }
    }

    @Override
    public int getSurcharge(final Station from, final Station to) {
        DijkstraShortestPath<Station, StationEdge> dijkstraShortestPath = createPath(lineRepository.findAll());
        GraphPath<Station, StationEdge> path = dijkstraShortestPath.getPath(from, to);
        return path.getEdgeList().stream()
                .mapToInt(StationEdge::getCost)
                .max()
                .orElseThrow(() -> new ApiNoSuchResourceException("요금을 찾을 수 없습니다."));
    }

    private DijkstraShortestPath<Station, StationEdge> createPath(final List<Line> lines) {
        WeightedMultigraph<Station, StationEdge> graph = new WeightedMultigraph<>(StationEdge.class);

        for (Line line : lines) {
            addVertex(graph, line);
            addEdges(graph, line);
        }
        return new DijkstraShortestPath<>(graph);
    }

    private void addEdges(final WeightedMultigraph<Station, StationEdge> graph, final Line line) {
        for (Section section : line.getSections()) {
            StationEdge stationEdge = new StationEdge(line.getCost());
            graph.addEdge(section.getUpStation(), section.getDownStation(), stationEdge);
            graph.setEdgeWeight(stationEdge, section.getDistance());
        }
    }

    private void addVertex(final WeightedMultigraph<Station, StationEdge> graph, final Line line) {
        Map<Station, Station> sectionsByMap = line.getSectionsByMap();
        for (Station station : sectionsByMap.keySet()) {
            graph.addVertex(station);
        }
        for (Station station : sectionsByMap.values()) {
            graph.addVertex(station);
        }
    }
}
