package subway.domain;

import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class ShortestPath {

    private final ShortestPathAlgorithm<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public ShortestPath(final Sections sections) {
        this.dijkstraShortestPath = createPath(sections);
    }

    private ShortestPathAlgorithm<Station, DefaultWeightedEdge> createPath(final Sections sections) {
        final Graph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        sections.getSections().forEach(
                section -> {
                    graph.addVertex(section.getUpStation());
                    graph.addVertex(section.getDownStation());
                    graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                            section.getDistance());
                }
        );
        return new DijkstraShortestPath<>(graph);
    }

    public List<Station> findShortestPath(final Station upStation, final Station downStation) {
        return dijkstraShortestPath.getPath(upStation, downStation).getVertexList();
    }

    public double getDistance(final Station upStation, final Station downStation) {
        return dijkstraShortestPath.getPathWeight(upStation, downStation);
    }
}
