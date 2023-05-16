package subway.domain.path;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.Station;
import subway.domain.section.Section;

@Component
public class DijkstraShortestPathFinder implements ShortestPathFinder {

    @Override
    public List<Section> findShortestPath(final List<Section> sections, final Station startStation,
                                          final Station endStation) {
        WeightedMultigraph<Station, SectionDijkstraEdge> graph
                = new WeightedMultigraph<>(SectionDijkstraEdge.class);
        initGraph(sections, graph);
        final DijkstraShortestPath<Station, SectionDijkstraEdge> shortestPath
                = new DijkstraShortestPath<>(graph);
        return shortestPath.getPath(startStation, endStation)
                .getEdgeList().stream()
                .map(SectionDijkstraEdge::getSection)
                .collect(toUnmodifiableList());
    }

    private void initGraph(final List<Section> sections, final WeightedMultigraph<Station, SectionDijkstraEdge> graph) {
        for (final Station station : findAllStation(sections)) {
            graph.addVertex(station);
        }
        for (final Section section : sections) {
            graph.addEdge(section.getPrevStation(), section.getNextStation(), new SectionDijkstraEdge(section));
        }
    }

    private Set<Station> findAllStation(final List<Section> sections) {
        final Set<Station> stations = new HashSet<>();
        for (final Section section : sections) {
            stations.add(section.getPrevStation());
            stations.add(section.getNextStation());
        }
        return stations;
    }
}
