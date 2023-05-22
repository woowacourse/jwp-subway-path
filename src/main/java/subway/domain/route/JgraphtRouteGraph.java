package subway.domain.route;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Distance;
import subway.domain.line.Line;
import subway.domain.line.Section;
import subway.domain.line.Station;

public class JgraphtRouteGraph implements RouteGraph {

    private final DijkstraShortestPath<Station, SectionEdge> sectionGraph;

    private JgraphtRouteGraph(DijkstraShortestPath<Station, SectionEdge> sectionGraph) {
        this.sectionGraph = sectionGraph;
    }

    public static JgraphtRouteGraph from(List<Line> lines) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);

        for (Line line : lines) {
            addStation(graph, line);
            addSection(graph, line);
        }

        return new JgraphtRouteGraph(new DijkstraShortestPath<>(graph));
    }

    private static void addStation(WeightedMultigraph<Station, SectionEdge> graph, Line line) {
        List<Station> stations = line.findLeftToRightRoute();

        stations.forEach(graph::addVertex);
    }

    private static void addSection(WeightedMultigraph<Station, SectionEdge> graph, Line line) {
        List<Section> sections = line.getSections();

        for (Section section : sections) {
            SectionEdge sectionEdge = new SectionEdge(line);
            graph.addEdge(section.getLeft(), section.getRight(), sectionEdge);

            SectionEdge edge = graph.getEdge(section.getLeft(), section.getRight());
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }

    @Override
    public List<Station> findShortestRoute(Station start, Station end) {
        GraphPath<Station, SectionEdge> path = sectionGraph.getPath(start, end);

        if (path == null) {
            throw new IllegalArgumentException(start.getName() + "과 " + end.getName() + " 사이의 경로가 존재하지 않습니다.");
        }

        return path.getVertexList();
    }

    @Override
    public Distance findShortestDistance(Station start, Station end) {
        return new Distance((int) sectionGraph.getPathWeight(start, end));
    }

    @Override
    public List<Line> findLinesOnRoutes(Station start, Station end) {
        List<SectionEdge> sectionEdges = sectionGraph.getPath(start, end).getEdgeList();

        return sectionEdges.stream()
                .map(SectionEdge::getLine)
                .collect(Collectors.toList());
    }
}
