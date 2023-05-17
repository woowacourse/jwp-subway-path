package subway.application.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JgraphtPathFinder implements PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public JgraphtPathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    @Override
    public ShortestPath findShortestPath(List<Section> sections, Station upStation, Station downStation) {
        final List<Station> allStations = sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());

        initializeGraph(allStations, sections);

        final DijkstraShortestPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph);

        final GraphPath<Station, DefaultWeightedEdge> shortestPath = path.getPath(upStation, downStation);
        return new ShortestPath(shortestPath.getVertexList(), Distance.from(shortestPath.getWeight()));
    }

    private void initializeGraph(List<Station> allStations, List<Section> sections) {
        for (Station station : allStations) {
            graph.addVertex(station);
        }

        for (Section section : sections) {
            final DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(edge, section.getDistance().getDistance());
        }
    }
}
