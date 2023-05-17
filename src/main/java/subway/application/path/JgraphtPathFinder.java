package subway.application.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.ShortestPath;
import subway.domain.Station;
import subway.domain.section.MultiLineSections;
import subway.domain.section.Section;

import java.util.List;

@Component
public class JgraphtPathFinder implements PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public JgraphtPathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    @Override
    public ShortestPath findShortestPath(MultiLineSections sections, Station upStation, Station downStation) {
        final List<Station> allStations = sections.getAllStations();
        initializeGraph(allStations, sections.getSections());

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
