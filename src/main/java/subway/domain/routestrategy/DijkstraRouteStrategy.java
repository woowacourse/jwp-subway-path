package subway.domain.routestrategy;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Subway;

@Component
public class DijkstraRouteStrategy implements RouteStrategy {
    
    
    @Override
    public List<Station> findShortestRoute(Subway subway, Station start, Station end) {
        WeightedMultigraph<Station, SubwaySection> graph = new WeightedMultigraph<>(
                SubwaySection.class);
        initialize(subway, graph);
        validateExistVertex(start, end, graph);
        return getStations(start, end, graph);
    }
    
    private void initialize(Subway subway, WeightedMultigraph<Station, SubwaySection> graph) {
        addStationsToGraph(subway, graph);
        addSectionsToGraph(subway, graph);
    }
    
    private void addStationsToGraph(Subway subway, WeightedMultigraph<Station, SubwaySection> graph) {
        for (Station station : subway.findAllStations()) {
            graph.addVertex(station);
        }
    }
    
    private void addSectionsToGraph(Subway subway, WeightedMultigraph<Station, SubwaySection> graph) {
        subway.getLines().stream()
                .forEach(line -> addSectionsOfLine(line, graph));
    }
    
    private void addSectionsOfLine(Line line, WeightedMultigraph<Station, SubwaySection> graph) {
        for (Section section : line.getSections()) {
            SubwaySection edge = new SubwaySection(section, line.getCharge());
            graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
            graph.setEdgeWeight(edge, section.getDistance().getValue());
        }
    }
    
    private void validateExistVertex(Station start, Station end, WeightedMultigraph<Station, SubwaySection> graph) {
        if (!graph.containsVertex(start) || !graph.containsVertex(end)) {
            throw new IllegalArgumentException("해당 역이 존재하지 않습니다.");
        }
    }
    
    private List<Station> getStations(Station start, Station end, WeightedMultigraph<Station, SubwaySection> graph) {
        GraphPath<Station, SubwaySection> path = new DijkstraShortestPath<>(graph).getPath(start, end);
        if (path == null) {
            throw new IllegalArgumentException("이동할 수 없는 경로입니다.");
        }
        return path.getVertexList();
    }
    
    @Override
    public Distance findShortestDistance(Subway subway, Station start, Station end) {
        WeightedMultigraph<Station, SubwaySection> graph = new WeightedMultigraph<>(
                SubwaySection.class);
        initialize(subway, graph);
        validateExistVertex(start, end, graph);
        return getDistance(start, end, graph);
    }
    
    private Distance getDistance(Station start, Station end,
            WeightedMultigraph<Station, SubwaySection> graph) {
        double shortestDistance = new DijkstraShortestPath<>(graph).getPathWeight(start, end);
        if (shortestDistance == Double.POSITIVE_INFINITY) {
            throw new IllegalArgumentException("이동할 수 없는 경로입니다.");
        }
        return new Distance((int) shortestDistance);
    }
    
    @Override
    public List<SubwaySection> findShortestSections(Subway subway, Station start, Station end) {
        WeightedMultigraph<Station, SubwaySection> graph = new WeightedMultigraph<>(
                SubwaySection.class);
        initialize(subway, graph);
        validateExistVertex(start, end, graph);
    
        GraphPath<Station, SubwaySection> path = new DijkstraShortestPath<>(graph).getPath(start, end);
        if (path == null) {
            throw new IllegalArgumentException("이동할 수 없는 경로입니다.");
        }
        return path.getEdgeList();
    }
    
}
