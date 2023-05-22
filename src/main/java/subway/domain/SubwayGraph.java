package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public SubwayGraph(final List<Section> sections) {
        this.dijkstraShortestPath = new DijkstraShortestPath<>(makeGraph(sections));
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeGraph(final List<Section> sections) {
        sections.forEach(
                section -> {
                    graph.addVertex(section.getUpStation());
                    graph.addVertex(section.getDownStation());
                    graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
                }
        );
        return graph;
    }

    public List<Station> getDijkstraShortestPath(final Station upStation, final Station downStation) {
        return dijkstraShortestPath.getPath(upStation, downStation).getVertexList();
    }

    public int getShortestPathWeight(final Station upStation, final Station downStation) {
        return (int) dijkstraShortestPath.getPathWeight(upStation, downStation);
    }
}
