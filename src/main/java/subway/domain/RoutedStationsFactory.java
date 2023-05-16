package subway.domain;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.jgrapht.graph.DefaultWeightedEdge;

public class RoutedStationsFactory {

    private RoutedStationsFactory() {
    }

    public static RoutedStations create(List<Section> sections) {
        validate(sections);

        RoutedStations stations = new RoutedStations(DefaultWeightedEdge.class);
        addVertex(sections, stations);
        addEdge(sections, stations);

        return stations;
    }

    private static void validate(final List<Section> sections) {
        Set<Station> leftStations = findStations(sections, Section::getLeft);
        Set<Station> rightStations = findStations(sections, Section::getRight);
        Set<Station> allStations = getAllStations(sections);

        if (!sections.isEmpty()
                && countStations(allStations, station -> isStart(station, leftStations, rightStations)) != 1) {
            throw new IllegalArgumentException("하행 종점은 1개여야 합니다.");
        }
        if (!sections.isEmpty()
                && countStations(allStations, station -> isEnd(station, leftStations, rightStations)) != 1) {
            throw new IllegalArgumentException("상행 종점은 1개여야 합니다.");
        }
    }

    private static int countStations(final Set<Station> stations, final Predicate<Station> filter) {
        return (int) stations.stream()
                .filter(filter)
                .count();
    }

    private static Set<Station> findStations(final List<Section> sections,
                                             final Function<? super Section, Station> mapper) {
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

    private static void addVertex(final List<Section> sections,
                                  final RoutedStations stations) {
        for (Station station : getAllStations(sections)) {
            stations.addVertex(station);
        }
    }

    private static Set<Station> getAllStations(List<Section> sections) {
        return sections.stream()
                .map(section -> List.of(section.getLeft(), section.getRight()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private static void addEdge(final List<Section> sections,
                                final RoutedStations stations) {
        for (Section section : sections) {
            stations.setEdgeWeight(
                    stations.addEdge(section.getLeft(), section.getRight()), section.getDistance().getValue());
        }
    }
}
