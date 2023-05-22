package subway.domain.routestrategy;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Station;
import subway.domain.Subway;

@Component
public class DijkstraRouteStrategy implements RouteStrategy {
    
    
    //todo : 왜 찾을 수 없는 경로는 null 뜨는지 내부 로직 뜯어보기
    //todo 2 : IllegalArgumentException이 아닌 예외 만들기
    @Override
    public List<Station> findShortestRoute(Subway subway, Station start, Station end) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
                DefaultWeightedEdge.class);
        initialize(subway, graph);
        validateExistVertex(start, end, graph);
        return getStations(start, end, graph);
    }
    
    private void initialize(Subway subway, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        addStationsToGraph(subway, graph);
        addSectionsToGraph(subway, graph);
    }
    
    private void addStationsToGraph(Subway subway, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Station station : subway.findAllStations()) {
            graph.addVertex(station);
        }
    }
    
    private void addSectionsToGraph(Subway subway, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        subway.getLines().stream()
                .flatMap(line -> line.getSections().stream())
                .distinct()
                .forEach(section -> graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation()),
                        section.getDistance().getValue()
                        )
                );
    }
    
    private void validateExistVertex(Station start, Station end, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        if (!graph.containsVertex(start) || !graph.containsVertex(end)) {
            throw new IllegalArgumentException("해당 역이 존재하지 않습니다.");
        }
    }
    
    private List<Station> getStations(Station start, Station end, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(start, end);
        if (path == null) {
            throw new IllegalArgumentException("이동할 수 없는 경로입니다.");
        }
        return path.getVertexList();
    }
    
    @Override
    public Distance findShortestDistance(Subway subway, Station start, Station end) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
                DefaultWeightedEdge.class);
        initialize(subway, graph);
        validateExistVertex(start, end, graph);
        return getDistance(start, end, graph);
    }
    
    private Distance getDistance(Station start, Station end,
            WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        double shortestDistance = new DijkstraShortestPath<>(graph).getPathWeight(start, end);
        if (shortestDistance == Double.POSITIVE_INFINITY) {
            throw new IllegalArgumentException("이동할 수 없는 경로입니다.");
        }
        return new Distance((int) shortestDistance);
    }
    
}
