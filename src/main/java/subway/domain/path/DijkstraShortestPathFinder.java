package subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.Stations;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DijkstraShortestPathFinder implements ShortestPathFinder {

    @Override
    public Path find(final Sections allSections, final Station startStation, final Station endStation) {
        final WeightedMultigraph<Station, Section> graph = initGraph(allSections.getAllSections());
        final GraphPath<Station, Section> shortestPath = findShortest(graph, startStation, endStation);
        final Sections edgeOrder = new Sections(shortestPath.getEdgeList());
        final Stations visitOrder = new Stations(shortestPath.getVertexList());
        return new Path(edgeOrder, visitOrder);
    }

    private WeightedMultigraph<Station, Section> initGraph(final List<Section> allSections) {
        final WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);
        initVertexes(allSections, graph);
        initEdges(allSections, graph);
        return graph;
    }

    private void initVertexes(final List<Section> allSections, final WeightedMultigraph<Station, Section> graph) {
        for (final Station station : getAllStations(allSections)) {
            graph.addVertex(station);
        }
    }

    private Set<Station> getAllStations(final List<Section> allSections) {
        return allSections.stream()
                .map(section -> List.of(section.getPreviousStation(), section.getNextStation()))
                .flatMap(List::stream)
                .collect(Collectors.toUnmodifiableSet());
    }

    private void initEdges(final List<Section> allSections, final WeightedMultigraph<Station, Section> graph) {
        for (final Section section : allSections) {
            graph.addEdge(section.getPreviousStation(), section.getNextStation(), section);
            graph.setEdgeWeight(section, section.getDistance().getLength());
        }
    }

    private GraphPath<Station, Section> findShortest(final WeightedMultigraph<Station, Section> graph,
                                            final Station startStation, final Station endStation) {
        final DijkstraShortestPath<Station, Section> shortestPath = new DijkstraShortestPath<>(graph);
        return shortestPath.getPath(startStation, endStation);
    }
}
