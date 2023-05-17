package subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class SubwayMap {

    private final MultiRoutedStations multiRoutedStations;

    public SubwayMap(final MultiRoutedStations multiRoutedStations) {
        this.multiRoutedStations = multiRoutedStations;
    }

    public RoutedStations findShortestRoutedStations(final Station sourceStation, final Station targetStation) {
        DijkstraShortestPath<Station, StationEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
                multiRoutedStations);

        List<StationEdge> shortestPathEdges = dijkstraShortestPath.getPath(sourceStation, targetStation)
                .getEdgeList();

        List<Section> sectionsFromShortestPath = shortestPathEdges.stream()
                .map(edge -> new Section(multiRoutedStations.getEdgeSource(edge),
                        multiRoutedStations.getEdgeTarget(edge),
                        new Distance((int) multiRoutedStations.getEdgeWeight(edge))))
                .collect(Collectors.toList());
        return RoutedStations.from(sectionsFromShortestPath);
    }
}
