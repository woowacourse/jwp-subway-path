package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.application.exception.SubwayServiceException;

import java.util.List;

public class Subways {

    private static final String INVALID_NO_STATION_MESSAGE = "노선에 존재하지 않는 역을 입력했습니다.";

    private final WeightedMultigraph<Station, SubwaysEdge> subways;

    private Subways(WeightedMultigraph<Station, SubwaysEdge> subways) {
        this.subways = subways;
    }

    public static Subways from(final List<Section> sections) {
        return new Subways(generateSubwayStructure(sections));
    }

    private static WeightedMultigraph<Station, SubwaysEdge> generateSubwayStructure(List<Section> sections) {
        WeightedMultigraph<Station, SubwaysEdge> subways = new WeightedMultigraph<>(SubwaysEdge.class);
        for (Section section : sections) {
            Station left = section.getLeft();
            Station right = section.getRight();
            SubwaysEdge subwaysEdge = new SubwaysEdge(section.getLine(), section.getDistance());

            subways.addVertex(left);
            subways.addVertex(right);
            subways.addEdge(left, right, subwaysEdge);
        }
        return subways;
    }

    public GraphPath<Station, SubwaysEdge> getShortestPaths(final Station start, final Station end) {
        validateGetPath(start, end);
        DijkstraShortestPath<Station, SubwaysEdge> dijkstraShortestPath = new DijkstraShortestPath<>(subways);
        return dijkstraShortestPath.getPath(start, end);
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
