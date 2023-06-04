package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayMap {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath;

    public SubwayMap(WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph) {
        this.shortestPath = new DijkstraShortestPath<>(stationGraph);
    }

    public Path getShortestPath(Station startStation, Station endStation) {
        GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(startStation, endStation);
        return new Path(path.getVertexList(), (int) path.getWeight());
    }
}
