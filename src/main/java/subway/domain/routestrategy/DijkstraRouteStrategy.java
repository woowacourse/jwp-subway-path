package subway.domain.routestrategy;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Subway;

@Component
public class DijkstraRouteStrategy implements RouteStrategy {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    //todo : 왜 찾을 수 없는 경로는 null 뜨는지 내부 로직 뜯어보기
    //todo 2 : IllegalArgumentException이 아닌 예외 만들기
    @Override
    public List<Station> findShortestRoute(Subway subway, Station start, Station end) {
        initialize(subway);
        validateExistVertex(start, end);
        GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(start, end);
        if (path == null) {
            throw new IllegalArgumentException("이동할 수 없는 경로입니다.");
        }
        return path.getVertexList();
    }

    private void validateExistVertex(Station start, Station end) {
        if (!graph.containsVertex(start) || !graph.containsVertex(end)) {
            throw new IllegalArgumentException("해당 역이 존재하지 않습니다.");
        }
    }

    private void initialize(Subway subway) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addStationsToGraph(subway);
        subway.getLines().stream()
                .flatMap(line -> line.getSections().stream())
                .distinct()
                .forEach(section -> addSectionsToGraph(section));
    }

    private void addStationsToGraph(Subway subway) {
        for (Station station : subway.findAllStations()) {
            System.out.println(station);
            graph.addVertex(station);
        }
    }

    private void addSectionsToGraph(Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance().getValue());
    }

    @Override
    public Distance findShortestDistance(Subway subway, Station start, Station end) {
        initialize(subway);
        validateExistVertex(start, end);
        double shortestDistance = new DijkstraShortestPath<>(graph).getPathWeight(start, end);
        if (shortestDistance == Double.POSITIVE_INFINITY) {
            throw new IllegalArgumentException("이동할 수 없는 경로입니다.");
        }
        return new Distance((int) shortestDistance);
    }

}
