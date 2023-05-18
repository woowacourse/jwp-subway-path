package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class StationGraph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public StationGraph(List<Sections> allSections) {
        for (Sections sections : allSections) {
            for (Section lineSection : sections.getSections()) {
                graph.addVertex(lineSection.getUpStation());
                graph.addVertex(lineSection.getDownStation());
                graph.setEdgeWeight(graph.addEdge(lineSection.getUpStation(), lineSection.getDownStation()), lineSection.getDistance());
            }
        }
    }

    public List<Station> findShortestPath(Station start, Station end) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(start, end).getVertexList();
    }

    public double findDistance(Station start, Station end) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPathWeight(start, end);
    }
}
