package subway.application.path;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.ShortestPath;
import subway.domain.Station;
import subway.domain.section.MultiLineSections;
import subway.domain.section.Section;

import java.util.List;

@Component
public class ShortestPathFinder implements PathFinder {

    private final Graph<Station, DefaultWeightedEdge> graph;
    private final ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm;

    public ShortestPathFinder(Graph<Station, DefaultWeightedEdge> graph, ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm) {
        this.graph = graph;
        this.shortestPathAlgorithm = shortestPathAlgorithm;
    }

    @Override
    public ShortestPath findShortestPath(MultiLineSections sections, Station upStation, Station downStation) {
        final List<Station> allStations = sections.getAllStations();
        initializeGraph(allStations, sections.getSections());

        final GraphPath<Station, DefaultWeightedEdge> shortestPath = shortestPathAlgorithm.getPath(upStation, downStation);
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
