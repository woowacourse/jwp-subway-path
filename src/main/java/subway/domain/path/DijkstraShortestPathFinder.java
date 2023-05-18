package subway.domain.path;

import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableSet;

import java.util.List;
import java.util.Set;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.Station;
import subway.domain.section.Section;

@Component
public class DijkstraShortestPathFinder implements ShortestPathFinder {

    @Override
    public Path findShortestPath(final List<Section> sections, final Station startStation,
                                 final Station endStation) {
        final WeightedMultigraph<Station, SectionDijkstraEdge> graph
                = new WeightedMultigraph<>(SectionDijkstraEdge.class);
        initGraph(sections, graph);
        final DijkstraShortestPath<Station, SectionDijkstraEdge> shortestPath
                = new DijkstraShortestPath<>(graph);

        final GraphPath<Station, SectionDijkstraEdge> graphPath = shortestPath.getPath(startStation, endStation);
        final List<Section> sectionList = graphPath.getEdgeList().stream()
                .map(SectionDijkstraEdge::getSection)
                .collect(toUnmodifiableList());
        return new Path(graphPath.getVertexList(), sectionList);
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
        return sections.stream()
                .map(section -> List.of(section.getNextStation(), section.getPrevStation()))
                .flatMap(List::stream)
                .collect(toUnmodifiableSet());
    }
}
