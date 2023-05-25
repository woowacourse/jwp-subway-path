package subway.line.domain.navigation.infrastructure;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import subway.line.domain.navigation.Path;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

import java.util.List;

public class JgraphtPath implements Path {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> path;

    public JgraphtPath(DijkstraShortestPath<Station, DefaultWeightedEdge> path) {
        this.path = path;
    }

    @Override
    public List<Station> findShortestPath(Station stationA, Station stationB) {
        return path
                .getPath(stationA, stationB)
                .getVertexList();
    }

    @Override
    public Distance findShortestDistance(Station stationA, Station stationB) {
        return Distance
                .of(path.getPath(stationA, stationB)
                        .getWeight());
    }
}
