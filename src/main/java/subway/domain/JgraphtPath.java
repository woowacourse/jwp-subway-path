package subway.domain;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class JgraphtPath implements GraphPath {

    @Override
    public List<String> getShortestPath(Sections sections, String fromStationName, String toStationName) {
        DijkstraShortestPath<String, DefaultWeightedEdge> graph = makeGraph(sections);
        return graph.getPath(fromStationName, toStationName).getVertexList();
    }

    @Override
    public Distance getShortestDistance(Sections sections, String fromStationName, String toStationName) {
        DijkstraShortestPath<String, DefaultWeightedEdge> graph = makeGraph(sections);
        Double weight = graph.getPath(fromStationName, toStationName).getWeight();
        return new Distance(weight.intValue());
    }

    private DijkstraShortestPath<String, DefaultWeightedEdge> makeGraph(final Sections sections) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Section section : sections.getSections()) {
            graph.addVertex(section.getFrom().getName());
            graph.addVertex(section.getTo().getName());
            graph.setEdgeWeight(graph.addEdge(section.getFrom().getName(), section.getTo().getName()),
                    section.getDistanceValue());
        }
        return new DijkstraShortestPath<>(graph);
    }

}
