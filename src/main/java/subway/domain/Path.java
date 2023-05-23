package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.exception.GlobalException;

import java.util.List;

public class Path {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph =
            new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath =
            new DijkstraShortestPath<>(graph);


    public Path(List<Station> stations, List<Section> sections) {
        initializeVertices(stations);
        initializeWeights(sections);
    }

    private void initializeVertices(List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void initializeWeights(List<Section> sections) {
        for (Section section : sections) {
            Station startStation = section.getStartStation();
            Station endStation = section.getEndStation();
            int distance = section.getDistance();

            graph.setEdgeWeight(graph.addEdge(startStation, endStation), distance);
        }
    }

    public List<Station> getShortestPath(Station startStation, Station endStation) {
        GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(startStation, endStation);

        if (path == null) {
            throw new GlobalException("이동할 수 없는 경로입니다.");
        }

        return path.getVertexList();
    }

    public Distance getShortestDistance(Station startStation, Station endStation) {
        GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(startStation, endStation);

        int distance = (int) path.getWeight();

        return new Distance(distance);
    }
}
