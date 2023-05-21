package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.application.exception.SubwayServiceException;

import java.util.List;

public class Subways {

    private static final String INVALID_NO_STATION_MESSAGE = "노선에 존재하지 않는 역을 입력했습니다.";

    private final WeightedMultigraph<Station, SubwayEdge> subways;

    private Subways(WeightedMultigraph<Station, SubwayEdge> subways) {
        this.subways = subways;
    }

    public static Subways from(final List<Section> sections) {
        WeightedMultigraph<Station, SubwayEdge> subwayStructure = generateSubwayStructure(sections);
        return new Subways(subwayStructure);
    }

    private static WeightedMultigraph<Station, SubwayEdge> generateSubwayStructure(List<Section> sections) {
        WeightedMultigraph<Station, SubwayEdge> subwayStructure = new WeightedMultigraph<>(SubwayEdge.class);
        for (Section section : sections) {
            Station left = section.getLeft();
            Station right = section.getRight();
            SubwayEdge subwayEdge = new SubwayEdge(section.getLine(), section.getDistance());

            subwayStructure.addVertex(left);
            subwayStructure.addVertex(right);
            subwayStructure.addEdge(left, right, subwayEdge);
        }
        return subwayStructure;
    }

    public GraphPath<Station, SubwayEdge> getShortestPaths(final Station start, final Station end) {
        validateGetPath(start, end);
        DijkstraShortestPath<Station, SubwayEdge> dijkstraShortestPath = new DijkstraShortestPath<>(subways);
        GraphPath<Station, SubwayEdge> paths = dijkstraShortestPath.getPath(start, end);
        return paths;
    }

    private void validateGetPath(Station start, Station end) {
        if (hasNoStation(start) || hasNoStation(end)) {
            throw new SubwayServiceException(INVALID_NO_STATION_MESSAGE);
        }
    }

    private boolean hasNoStation(Station station) {
        return !subways.containsVertex(station);
    }
}
