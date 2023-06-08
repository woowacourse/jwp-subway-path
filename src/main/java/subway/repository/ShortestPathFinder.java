package subway.repository;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Path;
import subway.domain.PathFinder;
import subway.domain.Section;
import subway.domain.Station;
import subway.exception.ErrorCode;
import subway.exception.NoSuchPath;

public class ShortestPathFinder implements PathFinder {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    private ShortestPathFinder(DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static ShortestPathFinder of(List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance()
            );
        }
        return new ShortestPathFinder(new DijkstraShortestPath<>(graph));
    }

    @Override
    public Path findPath(Station sourceStation, Station targetStation) {
        validateSameStation(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(sourceStation, targetStation);
        if (path == null) {
            throw new NoSuchPath(ErrorCode.NO_SUCH_PATH, sourceStation.getName(), targetStation.getName());
        }
        return new Path(path.getVertexList(), (int) path.getWeight());
    }
}
