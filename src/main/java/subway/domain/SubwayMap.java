package subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import subway.domain.exception.EmptyRoutedStationsSearchResult;

public class SubwayMap {

    private final MultiRoutedStations multiRoutedStations;

    public SubwayMap(final MultiRoutedStations multiRoutedStations) {
        this.multiRoutedStations = multiRoutedStations;
    }

    public RoutedStations findShortestRoutedStations(final Station sourceStation, final Station targetStation) {
        validateSourceAndTargetStation(sourceStation, targetStation);

        DijkstraShortestPath<Station, StationEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
                multiRoutedStations);
        List<StationEdge> shortestPathEdges = dijkstraShortestPath.getPath(sourceStation, targetStation)
                .getEdgeList();
        List<Section> sectionsFromShortestPath = sectionsFromShortestPath(shortestPathEdges);

        return RoutedStations.from(sectionsFromShortestPath);
    }

    private List<Section> sectionsFromShortestPath(List<StationEdge> shortestPathEdges) {
        return shortestPathEdges.stream()
                .map(edge -> new Section(multiRoutedStations.getEdgeSource(edge),
                        multiRoutedStations.getEdgeTarget(edge),
                        new Distance((int) multiRoutedStations.getEdgeWeight(edge))))
                .collect(Collectors.toList());
    }

    private void validateSourceAndTargetStation(final Station sourceStation, final Station targetStation) {
        if (!multiRoutedStations.containsVertex(sourceStation)) {
            throw new EmptyRoutedStationsSearchResult("지하철 노선도에 출발 역이 존재하지 않습니다.");
        }
        if (!multiRoutedStations.containsVertex(targetStation)) {
            throw new EmptyRoutedStationsSearchResult("지하철 노선도에 도착 역이 존재하지 않습니다.");
        }
    }

}
