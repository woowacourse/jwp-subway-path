package subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class RouteMap {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> sectionGraph;

    private RouteMap(DijkstraShortestPath<Station, DefaultWeightedEdge> sectionGraph) {
        this.sectionGraph = sectionGraph;
    }

    public static RouteMap from(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            addStation(graph, line);
            addSection(graph, line);
        }

        return new RouteMap(new DijkstraShortestPath<>(graph));
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

    public List<Station> findShortestRoute(Station start, Station end) {
        GraphPath<Station, DefaultWeightedEdge> path = sectionGraph.getPath(start, end);

        if (path == null) {
            throw new IllegalArgumentException(start.getName() + "과 " + end.getName() + " 사이의 경로가 존재하지 않습니다.");
        }
        return path.getVertexList();
    }

    public int findShortestDistance(Station start, Station end) {
        return (int) sectionGraph.getPathWeight(start, end);
    }
}
