package subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.exception.ErrorCode;
import subway.exception.InvalidException;
import subway.exception.NoSuchPath;

public class Path {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    private Path(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public static Path of(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance()
            );
        }
        return new Path(graph);
    }

    public List<Station> getShortestPath(Station sourceStation, Station targetStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);
        validateSameStation(sourceStation, targetStation);
        validatePossiblePath(path);
        return path.getVertexList();
    }

    public int getShortestPathDistance(Station sourceStation, Station targetStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);
        validateSameStation(sourceStation, targetStation);
        validatePossiblePath(path);
        return (int) path.getWeight();
    }

    private void validatePossiblePath(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new NoSuchPath(ErrorCode.NO_SUCH_PATH);
        }
    }

    private void validateSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new InvalidException(ErrorCode.INVALID_SAME_UP_AND_DOWN_STATION);
        }
    }
}
