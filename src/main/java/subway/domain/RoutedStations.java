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
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import subway.domain.entity.Section;
import subway.domain.entity.Station;
import subway.domain.exception.AbnormalRoutedStationsException;
import subway.domain.exception.EmptyRoutedStationsSearchResultException;
import subway.domain.exception.JgraphtException;

public class RoutedStations extends SimpleDirectedWeightedGraph<Station, SectionEdge> {

    private RoutedStations(final Class<SectionEdge> edgeClass) {
        super(edgeClass);
    }

    public static RoutedStations from(final List<Section> sections) {
        validateSectionsDuplication(sections);
        validateMultiRoutes(sections);

        RoutedStations stations = new RoutedStations(SectionEdge.class);
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
                                    final SimpleDirectedWeightedGraph<Station, SectionEdge> stations) {
        for (Station station : addingStations) {
            stations.addVertex(station);
        }
    }

    private static void addEdges(final List<Section> sections,
                                 final SimpleDirectedWeightedGraph<Station, SectionEdge> stations) {
        for (Section section : sections) {
            stations.setEdgeWeight(
                    stations.addEdge(section.getLeft(), section.getRight()), section.getDistance().getValue());
        }
    }

    public Optional<Section> findRightSection(Station station) {
        try {
            SectionEdge edge = outgoingEdgesOf(station).iterator().next();
            return Optional.of(Section.from(edge));
        } catch (NoSuchElementException exception) {
            return Optional.empty();
        }
    }

    public Optional<Section> findLeftSection(Station station) {
        try {
            SectionEdge edge = incomingEdgesOf(station).iterator().next();
            return Optional.of(Section.from(edge));
        } catch (NoSuchElementException exception) {
            return Optional.empty();
        }
    }

    public List<Section> extractSections() {
        return edgeSet().stream()
                .map(Section::from)
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
}
