package subway.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import subway.domain.vo.Distance;
import subway.domain.entity.Section;
import subway.domain.entity.Station;
import subway.domain.exception.EmptyRoutedStationsSearchResultException;
import subway.domain.exception.IllegalSubwayMapArgumentException;

public class SubwayMap {

    private final MultiRoutedStations multiRoutedStations;

    public SubwayMap(final MultiRoutedStations multiRoutedStations) {
        this.multiRoutedStations = multiRoutedStations;
    }

    public RoutedStations findShortestRoutedStations(final Station sourceStation, final Station targetStation) {
        // TODO 외부 라이브러리 자체적으로 비정상적인 경우에 예외를 던지는데, 그 예외를 전환시키기 vs 그와 상관없이 미리 모두 검증하기
        // 외부 라이브러리에서 던져주는 예외에 의존하면, 추후 라이브러리 변경 시 어려움이 클 것 같다.
        validateSourceAndTargetStation(sourceStation, targetStation);

        DijkstraShortestPath<Station, LineClassifiableEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
                multiRoutedStations);
        List<LineClassifiableEdge> shortestPathEdges = dijkstraShortestPath.getPath(sourceStation, targetStation)
                .getEdgeList();
        List<Section> sectionsFromShortestPath = sectionsFromShortestPath(shortestPathEdges);

        return RoutedStations.from(sectionsFromShortestPath);
    }

    private List<Section> sectionsFromShortestPath(List<LineClassifiableEdge> shortestPathEdges) {
        return shortestPathEdges.stream()
                .map(edge -> new Section(multiRoutedStations.getEdgeSource(edge),
                        multiRoutedStations.getEdgeTarget(edge),
                        new Distance((int) multiRoutedStations.getEdgeWeight(edge))))
                .collect(Collectors.toList());
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
}
