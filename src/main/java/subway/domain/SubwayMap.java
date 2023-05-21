package subway.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import subway.domain.entity.Section;
import subway.domain.entity.Station;
import subway.domain.exception.EmptyRoutedStationsSearchResultException;
import subway.domain.exception.IllegalSubwayMapArgumentException;
import subway.domain.exception.JgraphtException;
import subway.domain.vo.Distance;

public class SubwayMap {

    private final MultiRoutedStations multiRoutedStations;

    public SubwayMap(final MultiRoutedStations multiRoutedStations) {
        this.multiRoutedStations = multiRoutedStations;
    }

    public RoutedStations findShortestRoutedStations(final Station sourceStation, final Station targetStation) {
        validateSourceAndTargetStation(sourceStation, targetStation);

        List<LineClassifiableEdge> shortestPathEdges = shortestPathEdges(sourceStation, targetStation);
        List<Section> sectionsFromShortestPath = sectionsFromShortestPath(shortestPathEdges);

        return RoutedStations.from(sectionsFromShortestPath);
    }

    private void validateSourceAndTargetStation(final Station sourceStation, final Station targetStation) {
        if (!multiRoutedStations.containsVertex(sourceStation)) {
            throw new EmptyRoutedStationsSearchResultException("지하철 노선도에 출발 역이 존재하지 않습니다.");
        }
        if (!multiRoutedStations.containsVertex(targetStation)) {
            throw new EmptyRoutedStationsSearchResultException("지하철 노선도에 도착 역이 존재하지 않습니다.");
        }
        if (Objects.equals(sourceStation, targetStation)) {
            throw new IllegalSubwayMapArgumentException("출발 역과 도착 역이 동일한 경로를 찾을 수 없습니다.");
        }
    }

    private List<LineClassifiableEdge> shortestPathEdges(final Station sourceStation, final Station targetStation) {
        try {
            DijkstraShortestPath<Station, LineClassifiableEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
                    multiRoutedStations);
            return dijkstraShortestPath.getPath(sourceStation, targetStation)
                    .getEdgeList();
        } catch (RuntimeException exception) {
            throw new JgraphtException(exception.getMessage());
        }
    }

    private List<Section> sectionsFromShortestPath(List<LineClassifiableEdge> shortestPathEdges) {
        return shortestPathEdges.stream()
                .map(edge -> new Section(multiRoutedStations.getEdgeSource(edge),
                        multiRoutedStations.getEdgeTarget(edge),
                        new Distance((int) multiRoutedStations.getEdgeWeight(edge))))
                .collect(Collectors.toList());
    }
}
