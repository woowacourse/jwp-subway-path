package subway.domain;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class RoutedStationsFactory {

    public static SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> create(List<Section> sections) {
        SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations = new SimpleDirectedWeightedGraph<>(
                DefaultWeightedEdge.class);

        addVertex(sections, stations);
        addEdge(sections, stations);

        return stations;
    }

    private static void addVertex(final List<Section> sections,
                                  final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations) {
        for (Station station : getAllStations(sections)) {
            stations.addVertex(station);
        }
    }

    private static void addEdge(final List<Section> sections,
                                final SimpleDirectedWeightedGraph<Station, DefaultWeightedEdge> stations) {
        for (Section section : sections) {
            stations.setEdgeWeight(
                    stations.addEdge(section.getLeft(), section.getRight()), section.getDistance().getValue());
        }
    }

    private static Set<Station> getAllStations(List<Section> sections) {
        return sections.stream()
                .map(section -> List.of(section.getLeft(), section.getRight()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
