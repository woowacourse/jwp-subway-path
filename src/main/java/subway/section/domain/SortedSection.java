package subway.section.domain;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.station.domain.Station;

public class SortedSection {
    private final List<Station> stations;

    public SortedSection(final Sections sections) {
        this.stations = sort(sections.getSections());
    }

    private List<Station> sort(final List<Section> sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = initGraph(sections);

        final Station frontStation = findFrontStation(sections);
        final Station endStation = findEndStation(sections);

        final DijkstraShortestPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph);
        return path.getPath(frontStation, endStation).getVertexList();
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> initGraph(final List<Section> sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
                DefaultWeightedEdge.class);

        for (final Section section : sections) {
            final Station firstStation = section.getFirstStation();
            final Station secondStation = section.getSecondStation();
            graph.addVertex(firstStation);
            graph.addVertex(secondStation);
            graph.setEdgeWeight(graph.addEdge(firstStation, secondStation), section.getDistance().getDistance());
        }
        return graph;
    }

    private Station findFrontStation(final List<Section> sections) {
        final Set<Station> sectionSet = sections.stream()
                .map(Section::getSecondStation)
                .collect(Collectors.toSet());

        return sections.stream()
                .map(Section::getFirstStation)
                .filter(Predicate.not(sectionSet::contains))
                .findFirst()
                .orElseThrow();
    }

    private Station findEndStation(final List<Section> sections) {
        final Set<Station> sectionSet = sections.stream()
                .map(Section::getFirstStation)
                .collect(Collectors.toSet());

        return sections.stream()
                .map(Section::getSecondStation)
                .filter(Predicate.not(sectionSet::contains))
                .findFirst()
                .orElseThrow();
    }

    public List<Station> getStations() {
        return stations;
    }
}
