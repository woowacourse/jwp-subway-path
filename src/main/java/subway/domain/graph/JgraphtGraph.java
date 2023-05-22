package subway.domain.graph;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

public class JgraphtGraph implements Graph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public JgraphtGraph(final List<Section> sections) {
        makeGraph(sections);
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    @Override
    public void makeGraph(final List<Section> sections) {
        sections.forEach(
                section -> {
                    addVertex(section.getUpStation());
                    addVertex(section.getDownStation());
                    addEdge(section.getUpStation(), section.getDownStation(), section.getDistance());
                }
        );
    }

    @Override
    public void addVertex(final Station vertex) {
        graph.addVertex(vertex);
    }

    @Override
    public void addEdge(final Station source, final Station target, final int edge) {
        graph.setEdgeWeight(graph.addEdge(source, target), edge);
    }

    @Override
    public List<Station> findShortestPath(final Station source, final Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    @Override
    public int findShortestPathWeight(final Station source, final Station target) {
        return (int) dijkstraShortestPath.getPathWeight(source, target);
    }
}
