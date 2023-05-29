package subway.domain.graph;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.section.Section;
import subway.domain.station.Station;

public class JgraphtGraph implements SubwayGraph {
    @Override
    public List<Station> getPath(final List<Section> sections, final Station source, final Station destination) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = createGraph(sections);
        validateVertex(graph, source, destination);

        DijkstraShortestPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph);
        return path.getPath(source, destination).getVertexList();
    }

    @Override
    public int getWeight(final List<Section> sections, final Station source, final Station destination) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = createGraph(sections);
        validateVertex(graph, source, destination);

        DijkstraShortestPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph);
        return (int) path.getPathWeight(source, destination);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> createGraph(final List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        sections.forEach(section -> addElement(graph, section));
        return graph;
    }

    private void addElement(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance().getValue());
    }

    private void validateVertex(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final Station source, final Station destination) {
        if (!graph.containsVertex(source) || !graph.containsVertex(destination)) {
            throw new IllegalArgumentException("그래프에 역이 존재하지 않습니다.");
        }
    }
}
