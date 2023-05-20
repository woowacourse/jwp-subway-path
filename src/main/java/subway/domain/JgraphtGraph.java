package subway.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class JgraphtGraph implements Graph {
    private final WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    @Override
    public void set(List<Station> stations, List<Section> sections) {
        for (Station station: stations) {
            graph.addVertex(station.getName());
        }
        for (Section section: sections) {
            graph.setEdgeWeight(graph.addEdge(section.getStartStation().getName(),
                            section.getEndStation().getName()),
                    section.getDistance().getDistance());
        }
    }

    @Override
    public List<String> findPath(String start, String end){
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        return dijkstraShortestPath.getPath(start, end).getVertexList();
    }

    @Override
    public double findPathDistance(String start, String end){
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        return dijkstraShortestPath.getPath(start, end).getWeight();
    }
}
