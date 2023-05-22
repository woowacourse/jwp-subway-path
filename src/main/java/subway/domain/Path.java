package subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.exception.ErrorCode;
import subway.exception.NoSuchPath;

public class Path {
    public static final int DEFAULT_FARE = 1250;
    public static final int UNIT_OVER_TEN = 5;
    public static final int UNIT_OVER_FIFTY = 8;
    public static final int ADD_FARE = 100;

    private final WeightedMultigraph<String, DefaultWeightedEdge> graph;

    private Path(WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static Path of(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Station station : stations) {
            graph.addVertex(station.getName());
        }

        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(
                    section.getUpStation().getName(),
                    section.getDownStation().getName()
            ),section.getDistance());
        }

        return new Path(graph);
    }

    public List<String> getDijkstraShortestPath(String sourceStation, String targetStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<String, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);
        if(path == null) {
            throw new NoSuchPath(ErrorCode.NO_SUCH_PATH);
        }
        return path.getVertexList();
    }

    public int getDijkstraShortestPathDistance(String sourceStation, String targetStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<String, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);
        if (path == null) {
            throw new NoSuchPath(ErrorCode.NO_SUCH_PATH);
        }
        return (int) path.getWeight();
    }

    public int calculateFare(int distance) {
        int fare = DEFAULT_FARE;
        fare += (calculateOverFare(distance - 10, UNIT_OVER_TEN) - calculateOverFare(distance - 50, UNIT_OVER_TEN));
        fare += calculateOverFare(distance - 50, UNIT_OVER_FIFTY);

        return fare;
    }

    private int calculateOverFare(int distance, int unit) {
        if (distance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * ADD_FARE);
    }
}
