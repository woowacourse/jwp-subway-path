package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    public Path(WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph) {
        this.shortestPath = new DijkstraShortestPath<>(stationGraph);
    }

    public GraphPath<Station, DefaultWeightedEdge> getShortestPath(Station startStation, Station endStation) {
        return shortestPath.getPath(startStation, endStation);
    }
}
