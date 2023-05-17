package subway.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class MultiRoutedStationsFactory {

    private MultiRoutedStationsFactory() {
    }

    // TODO Section이 Line을 가지면 Map으로 조회해 전달할 필요가 없는데 뭐가 더 좋을까?
    public static MultiRoutedStations create(Map<Line, List<Section>> sectionsByLine) {
        MultiRoutedStations stations = new MultiRoutedStations(StationEdge.class);

        Set<Station> addingStations = getAllStations(new ArrayList<>(sectionsByLine.values()));
        addVertex(addingStations, stations);
        for (Entry<Line, List<Section>> entry : sectionsByLine.entrySet()) {
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

    private static Set<Station> getAllStations(final List<List<Section>> sections) {
        return sections.stream()
                .flatMap(Collection::stream)
                .map(section -> List.of(section.getLeft(), section.getRight()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private static void addEdge(final Line line,
                                final List<Section> sections,
                                final MultiRoutedStations stations) {
        for (Section section : sections) {
            StationEdge stationEdge = new StationEdge(line);
            stations.addEdge(section.getLeft(), section.getRight(), stationEdge);
            stations.setEdgeWeight(stationEdge, section.getDistance().getValue());
        }
    }
}
