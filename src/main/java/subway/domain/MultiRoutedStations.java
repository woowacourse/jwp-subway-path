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

public class MultiRoutedStations extends WeightedMultigraph<Station, LineClassifiableEdge> {

    private MultiRoutedStations(final Class<LineClassifiableEdge> edgeClass) {
        super(edgeClass);
    }

    // TODO Section이 Line을 가지면 Map으로 조회해 전달할 필요가 없는데 뭐가 더 좋을까?
    public static MultiRoutedStations from(Map<Line, RoutedStations> sectionsByLine) {
        MultiRoutedStations stations = new MultiRoutedStations(LineClassifiableEdge.class);

        Set<Station> addingStations = getAllStations(new ArrayList<>(sectionsByLine.values()));
        addVertex(addingStations, stations);
        for (Entry<Line, RoutedStations> entry : sectionsByLine.entrySet()) {
            addEdge(entry.getKey(), entry.getValue(), stations);
        }

        return stations;
    }

    private static void addVertex(final Set<Station> adding,
                                  final MultiRoutedStations stations) {
        for (Station station : adding) {
            stations.addVertex(station);
        }
    }

    private static Set<Station> getAllStations(final List<RoutedStations> routedStations) {
        return routedStations.stream()
                .map(AbstractBaseGraph::vertexSet)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private static void addEdge(final Line line,
                                final RoutedStations routedStations,
                                final MultiRoutedStations stations) {
        for (Section section : routedStations.extractSections()) {
            LineClassifiableEdge lineClassifiableEdge = new LineClassifiableEdge(line);
            stations.addEdge(section.getLeft(), section.getRight(), lineClassifiableEdge);
            stations.setEdgeWeight(lineClassifiableEdge, section.getDistance().getValue());
        }
    }
}
