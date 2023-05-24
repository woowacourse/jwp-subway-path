package subway.domain;

import java.util.Objects;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import subway.domain.entity.Station;
import subway.domain.exception.EmptyRoutedStationsSearchResultException;
import subway.domain.exception.IllegalSubwayMapArgumentException;
import subway.domain.exception.JgraphtException;

public class SubwayMap {

    private final MultiRoutedStations multiRoutedStations;

    public SubwayMap(final MultiRoutedStations multiRoutedStations) {
        this.multiRoutedStations = multiRoutedStations;
    }

    public TransferableRoute findShortestRoute(final Station sourceStation, final Station targetStation) {
        validateSourceAndTargetStation(sourceStation, targetStation);
        return findShortestPath(sourceStation, targetStation);
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

    private TransferableRoute findShortestPath(final Station sourceStation, final Station targetStation) {
        try {
            DijkstraShortestPath<Station, LineClassifiableSectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
                    multiRoutedStations);
            return new TransferableRoute(dijkstraShortestPath.getPath(sourceStation, targetStation));
        } catch (RuntimeException exception) {
            throw new JgraphtException(exception.getMessage());
        }
    }
}
