package subway.domain.routestrategy;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

public class DijkstraRouteStrategy implements RouteStrategy {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    //todo : 왜 찾을 수 없는 경로는 null 뜨는지 내부 로직 뜯어보기
    //todo 2 : IllegalArgumentException이 아닌 예외 만들기
    @Override
    public List<Station> findShortestRoute(List<Line> lines, Station start, Station end) {
        initialize(lines);
        validateExistVertex(start, end, graph);
        GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(start, end);
        if (path == null) {
            throw new IllegalArgumentException("이동할 수 없는 경로입니다.");
        }
        return path.getVertexList();
    }

    private static void validateExistVertex(Station start, Station end,
            WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        if (!graph.containsVertex(start) || !graph.containsVertex(end)) {
            throw new IllegalArgumentException("해당 역이 존재하지 않습니다.");
        }
    }

    private void initialize(List<Line> lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.forEach(line -> addStationsToGraph(line, graph));
        lines.forEach(line -> addSectionsToGraph(line, graph));
    }

    private void addStationsToGraph(Line line, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Station station : line.findStations()) {
            graph.addVertex(station);
        }
    }

    private void addSectionsToGraph(Line line, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : line.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance().getValue());
        }
    }

    @Override
    public Distance findShortestDistance(List<Line> lines, Station start, Station end) {
        initialize(lines);
        validateExistVertex(start, end, graph);
        double shortestDistance = new DijkstraShortestPath<>(graph).getPathWeight(start, end);
        if(shortestDistance == Double.POSITIVE_INFINITY) {
            throw new IllegalArgumentException("이동할 수 없는 경로입니다.");
        }
        return new Distance((int)shortestDistance);
    }
}
