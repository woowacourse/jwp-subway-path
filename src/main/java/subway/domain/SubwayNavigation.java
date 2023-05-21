package subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.controller.exception.PathNotFoundException;
import subway.controller.exception.StationNotFoundException;

public class SubwayNavigation implements Navigation {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private SubwayNavigation(final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static SubwayNavigation from(final List<Sections> sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        initialize(sections, graph);
        return new SubwayNavigation(graph);
    }

    private static void initialize(
            final List<Sections> allSections,
            final WeightedMultigraph<Station, DefaultWeightedEdge> graph
    ) {
        for (Sections sections : allSections) {
            initializeSections(graph, sections);
        }
    }

    private static void initializeSections(
            final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
            final Sections sections
    ) {
        for (Section section : sections.get()) {
            final Station source = section.getSource();
            final Station target = section.getTarget();
            graph.addVertex(source);
            graph.addVertex(target);
            graph.setEdgeWeight(graph.addEdge(source, target), section.getDistance());
        }
    }

    @Override
    public List<Station> getShortestPath(final Station source, final Station target) {
        validateStations(source, target);
        final DijkstraShortestPath<Station, Station> dijkstraShortestPath = new DijkstraShortestPath(graph);
        final GraphPath<Station, Station> path = dijkstraShortestPath.getPath(source, target);
        validatePath(path);
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    private void validateStations(final Station source, final Station target) {
        if (!graph.containsVertex(source)) {
            throw new StationNotFoundException("출발역이 존재하지 않습니다.");
        }
        if (!graph.containsVertex(target)) {
            throw new StationNotFoundException("도착역이 존재하지 않습니다.");
        }
    }

    private void validatePath(final GraphPath path) {
        if (path == null) {
            throw new PathNotFoundException();
        }
    }

    @Override
    public int getDistance(final Station source, final Station target) {
        validateStations(source, target);
        final DijkstraShortestPath<Station, Station> dijkstraShortestPath = new DijkstraShortestPath(graph);
        final GraphPath<Station, Station> path = dijkstraShortestPath.getPath(source, target);
        validatePath(path);
        return (int) path.getWeight();
    }
}
