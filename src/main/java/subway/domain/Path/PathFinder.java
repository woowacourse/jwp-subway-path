package subway.domain.Path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.section.Section;
import subway.domain.station.Station;

import java.util.List;

public class PathFinder {

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static PathFinder from(List<Section> allSections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);

        if (allSections.size() == 0) {
            throw new IllegalArgumentException("[ERROR] 경로를 찾을 구간들이 존재하지 않습니다.");
        }

        for (Section section : allSections) {
            graph.addVertex(section.getUpward());
            graph.addVertex(section.getDownward());
            graph.setEdgeWeight(graph.addEdge(section.getUpward(), section.getDownward()), section.getDistance());
        }

        return new PathFinder(new DijkstraShortestPath<>(graph));
    }

    public List<Station> findShortestPath(Station source, Station destination) {
        validateStations(source, destination);

        return dijkstraShortestPath.getPath(source, destination).getVertexList();
    }

    private static void validateStations(Station source, Station destination) {
        if (source.equals(destination)) {
            throw new IllegalArgumentException("[ERROR] 동일한 역 간 경로 조회는 불가능합니다.");
        }
    }

    public int findShortestDistance(Station source, Station destination) {
        validateStations(source, destination);
        return (int) dijkstraShortestPath.getPathWeight(source, destination);
    }
}
