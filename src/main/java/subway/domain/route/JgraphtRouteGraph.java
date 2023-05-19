package subway.domain.route;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Distance;
import subway.domain.line.Line;
import subway.domain.line.Section;
import subway.domain.line.Station;

public class JgraphtRouteGraph implements RouteGraph {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> sectionGraph;

    private JgraphtRouteGraph(DijkstraShortestPath<Station, DefaultWeightedEdge> sectionGraph) {
        this.sectionGraph = sectionGraph;
    }

    public static JgraphtRouteGraph from(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            addStation(graph, line);
            addSection(graph, line);
        }

        return new JgraphtRouteGraph(new DijkstraShortestPath<>(graph));
    }

    private static void addStation(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        List<Station> stations = line.findLeftToRightRoute();

        stations.forEach(graph::addVertex);
    }

    private static void addSection(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        List<Section> sections = line.getSections();

        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getLeft(), section.getRight()), section.getDistance());
        }
    }

    @Override
    public List<Station> findShortestRoute(Station start, Station end) {
        GraphPath<Station, DefaultWeightedEdge> path = sectionGraph.getPath(start, end);

        if (path == null) {
            throw new IllegalArgumentException(start.getName() + "과 " + end.getName() + " 사이의 경로가 존재하지 않습니다.");
        }
        return path.getVertexList();
    }

    @Override
    public Distance findShortestDistance(Station start, Station end) {
        return new Distance((int) sectionGraph.getPathWeight(start, end));
    }
}
