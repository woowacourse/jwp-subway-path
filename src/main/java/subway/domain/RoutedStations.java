package subway.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import subway.domain.entity.Section;
import subway.domain.entity.Station;
import subway.domain.exception.AbnormalRoutedStationsException;
import subway.domain.exception.EmptyRoutedStationsSearchResultException;
import subway.domain.exception.JgraphtException;
import subway.domain.vo.Distance;

// TODO 노선 별 요금 적용을 위해 StationEdge로 변경, 생성할 때 Line 전달받아 Edge에 저장하기 or Section이 Line을 가지면 해결되는 문제 아닌가?
// Section이 Line을 가지면, Line이 사실상 비즈니스 로직에 필요 없을 때에도 Line을 저장해야 한다 (노선 역 등록/삭제)
// 그래프에서 필요에 따라 edge에 Line을 저장하게 만들자. => 필요에 따라 Line을 가지고 있는 edge, Line을 가지고 있지 않은 edge를 사용하도록 함
// StationEdge -> LinedClassifiableWeightedEdge
public class RoutedStations extends SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> {

    private RoutedStations(final Class<DefaultWeightedEdge> edgeClass) {
        super(edgeClass);
    }

    public static RoutedStations from(final List<Section> sections) {
        validateSectionsDuplication(sections);
        validateMultiRoutes(sections);

        RoutedStations stations = new RoutedStations(DefaultWeightedEdge.class);
        Set<Station> addingStations = getAllStations(sections);
        try {
            addVertexes(addingStations, stations);
            addEdges(sections, stations);
            return stations;
        } catch (RuntimeException exception) {
            throw new JgraphtException(exception.getMessage());
        }
    }

    private static void validateSectionsDuplication(final List<Section> sections) {
        if (sections.size() != new HashSet<>(sections).size()) {
            throw new AbnormalRoutedStationsException("동일한 두 점을 연결하는 구간이 존재합니다.");
        }
    }

    private static void validateMultiRoutes(final List<Section> sections) {
        Set<Station> leftStations = findStations(sections, Section::getLeft);
        Set<Station> rightStations = findStations(sections, Section::getRight);
        Set<Station> allStations = getAllStations(sections);

        if (!sections.isEmpty()
                && countStations(allStations, station -> isStart(station, leftStations, rightStations)) != 1) {
            throw new AbnormalRoutedStationsException("하행 종점은 1개여야 합니다.");
        }
        if (!sections.isEmpty()
                && countStations(allStations, station -> isEnd(station, leftStations, rightStations)) != 1) {
            throw new AbnormalRoutedStationsException("상행 종점은 1개여야 합니다.");
        }
    }

    private static int countStations(final Set<Station> stations, final Predicate<Station> filter) {
        return (int) stations.stream()
                .filter(filter)
                .count();
    }

    private static Set<Station> findStations(final List<Section> sections,
                                             final Function<Section, Station> mapper) {
        return sections.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }

    private static boolean isStart(final Station station,
                                   final Set<Station> leftStations,
                                   final Set<Station> rightStations) {
        return leftStations.contains(station) && !rightStations.contains(station);
    }

    private static boolean isEnd(final Station station,
                                 final Set<Station> leftStations,
                                 final Set<Station> rightStations) {
        return !leftStations.contains(station) && rightStations.contains(station);
    }

    private static Set<Station> getAllStations(List<Section> sections) {
        return sections.stream()
                .map(Section::getStations)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private static void addVertexes(final Set<Station> addingStations,
                                    final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations) {
        for (Station station : addingStations) {
            stations.addVertex(station);
        }
    }

    private static void addEdges(final List<Section> sections,
                                 final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations) {
        for (Section section : sections) {
            stations.setEdgeWeight(
                    stations.addEdge(section.getLeft(), section.getRight()), section.getDistance().getValue());
        }
    }

    public Optional<Section> findRightSection(Station station) {
        try {
            DefaultWeightedEdge edge = outgoingEdgesOf(station).iterator().next();
            return Optional.of(
                    new Section(station, getEdgeTarget(edge), new Distance((int) getEdgeWeight(edge)))
            );
        } catch (NoSuchElementException exception) {
            return Optional.empty();
        }
    }

    public Optional<Section> findLeftSection(Station station) {
        try {
            DefaultWeightedEdge edge = incomingEdgesOf(station).iterator().next();
            return Optional.of(
                    new Section(getEdgeSource(edge), station, new Distance((int) getEdgeWeight(edge)))
            );
        } catch (NoSuchElementException exception) {
            return Optional.empty();
        }
    }

    public List<Section> extractSections() {
        return edgeSet().stream()
                .map(edge -> new Section(getEdgeSource(edge), getEdgeTarget(edge),
                        new Distance((int) getEdgeWeight(edge))))
                .collect(Collectors.toList());
    }

    public List<Station> extractOrderedStations() {
        List<Station> orderedStations = new ArrayList<>();
        if (isEmpty()) {
            return orderedStations;
        }

        Station station = findStart().orElseThrow(
                () -> new EmptyRoutedStationsSearchResultException("하행 종점을 찾을 수 없습니다."));
        while (!outgoingEdgesOf(station).isEmpty()) {
            orderedStations.add(station);
            Section rightSection = findRightSection(station).get();
            station = rightSection.getRight();
        }
        orderedStations.add(station);

        return orderedStations;
    }

    private Optional<Station> findStart() {
        return vertexSet().stream()
                .filter(station -> incomingEdgesOf(station).isEmpty())
                .findFirst();
    }

    public boolean isEmpty() {
        return edgeSet().isEmpty();
    }

    public Distance totalDistance() {
        int sum = edgeSet().stream()
                .mapToInt(edge -> (int) getEdgeWeight(edge))
                .sum();
        return new Distance(sum);
    }
}
