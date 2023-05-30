package subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class ShortestPathFinder {

    private final Subway subway;

    public ShortestPathFinder(final Subway subway) {
        this.subway = subway;
    }

    public ShortestPath find(final String start, final String end) {
        final GraphPath<String, DefaultWeightedEdge> path = initShortestPath().getPath(start, end);
        final List<Station> stations = path.getVertexList().stream()
                .map(Station::new)
                .collect(Collectors.toList());
        return new ShortestPath(stations, (long) path.getWeight());
    }

    private DijkstraShortestPath<String, DefaultWeightedEdge> initShortestPath() {
        final WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertices(graph);
        subway.getSubway().forEach(sections -> setEdges(graph, sections));
        return new DijkstraShortestPath<>(graph);
    }

    private void addVertices(final WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        subway.getStations().stream()
                .map(Station::getName)
                .forEach(graph::addVertex);
    }

    private void setEdges(final WeightedMultigraph<String, DefaultWeightedEdge> graph, final Sections sections) {
        for (final Section section : sections.getSections()) {
            final DefaultWeightedEdge edge = graph.addEdge(section.getUpStation().getName(), section.getDownStation().getName());
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }
}
