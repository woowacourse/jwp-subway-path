package subway.line.domain.navigation.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.line.domain.section.Section;
import subway.line.domain.station.Station;

public class JgraphtGraph implements SubwayGraph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public JgraphtGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    @Override
    public void addPath(Section section) {
        if (!section.isNextStationEmpty()) {
            graph.addVertex(section.getPreviousStation());
            graph.addVertex(section.getNextStation());
            final var e = graph.addEdge(section.getPreviousStation(), section.getNextStation());
            graph.setEdgeWeight(e, section.getDistance().getValue());
        }
    }

    @Override
    public Path makePath() {
        return new JgraphtPath(new DijkstraShortestPath<>(graph));
    }
}
