package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Map;

public class ShortestRouteFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public ShortestRouteFinder(final List<Line> lines) {
        for (Line line : lines) {
            addVertex(line);
            addEdges(line);
        }
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public List<Station> getRoute(final Station from, final Station to) {
        return dijkstraShortestPath.getPath(from, to).getVertexList();
    }

    public int getDistance(final Station from, final Station to) {
        return (int) dijkstraShortestPath.getPath(from, to).getWeight();
    }

    private void addEdges(Line line) {
        for (Section section : line.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private void addVertex(final Line line) {
        Map<Station, Station> sectionsByMap = line.getSectionsByMap();
        for (Station station : sectionsByMap.keySet()) {
            graph.addVertex(station);
        }
        for (Station station : sectionsByMap.values()) {
            graph.addVertex(station);
        }
    }
}
