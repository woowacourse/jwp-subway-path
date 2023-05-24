package subway.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.entity.Line;
import subway.domain.entity.Section;
import subway.domain.entity.Station;
import subway.domain.exception.JgraphtException;

public class MultiRoutedStations extends WeightedMultigraph<Station, LineClassifiableSectionEdge> {

    private MultiRoutedStations(final Class<LineClassifiableSectionEdge> edgeClass) {
        super(edgeClass);
    }

    public static MultiRoutedStations from(Map<Line, RoutedStations> sectionsByLine) {
        MultiRoutedStations stations = new MultiRoutedStations(LineClassifiableSectionEdge.class);
        Set<Station> addingStations = getAllStations(new ArrayList<>(sectionsByLine.values()));

        try {
            addVertex(addingStations, stations);
            addEdges(sectionsByLine, stations);
            return stations;
        } catch (RuntimeException exception) {
            throw new JgraphtException(exception.getMessage());
        }
    }

    private static Set<Station> getAllStations(final List<RoutedStations> routedStations) {
        return routedStations.stream()
                .map(AbstractBaseGraph::vertexSet)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private static void addVertex(final Set<Station> adding,
                                  final MultiRoutedStations stations) {
        for (Station station : adding) {
            stations.addVertex(station);
        }
    }

    private static void addEdges(final Map<Line, RoutedStations> sectionsByLine, final MultiRoutedStations stations) {
        for (Entry<Line, RoutedStations> entry : sectionsByLine.entrySet()) {
            addEdge(entry.getKey(), entry.getValue(), stations);
        }
    }

    private static void addEdge(final Line line,
                                final RoutedStations routedStations,
                                final MultiRoutedStations stations) {
        for (Section section : routedStations.extractSections()) {
            LineClassifiableSectionEdge lineClassifiableSectionEdge = new LineClassifiableSectionEdge(line);
            stations.addEdge(section.getLeft(), section.getRight(), lineClassifiableSectionEdge);
            stations.setEdgeWeight(lineClassifiableSectionEdge, section.getDistance().getValue());
        }
    }
}
