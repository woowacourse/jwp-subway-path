package subway.domain;

import java.util.LinkedList;
import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    public PathFinder(final List<LinkedList<Section>> allSections) {
        this.shortestPath = new DijkstraShortestPath<>(makeGraph(allSections));
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeGraph(final List<LinkedList<Section>> allSections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (final LinkedList<Section> sections : allSections) {
            for (final Section section : sections) {
                graph.addVertex(section.getLeft());
                graph.addVertex(section.getRight());
                graph.setEdgeWeight(graph.addEdge(section.getLeft(), section.getRight()), section.getDistance());
            }
        }
        return graph;
    }

    public int getPathDistance(final Station from, final Station to) {
        return (int) shortestPath.getPathWeight(from, to);
    }

    public List<Station> getStations(final Station from, final Station to) {
        return shortestPath.getPath(from, to)
                .getVertexList();
    }
}
