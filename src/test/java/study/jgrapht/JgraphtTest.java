package study.jgrapht;

import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

public class JgraphtTest {

    @Test
    void testJgrapht() {
        //given
        final Graph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        //when
        graph.addVertex("a1");
        graph.addVertex("a2");
        graph.addVertex("a3");
        graph.setEdgeWeight(graph.addEdge("a1", "a2"), 2D);
        graph.setEdgeWeight(graph.addEdge("a2", "a3"), 4D);
        graph.setEdgeWeight(graph.addEdge("a1", "a3"), 10D);

        //then
        final DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath
            = new DijkstraShortestPath<>(graph);
        final GraphPath<String, DefaultWeightedEdge> shortestPath = dijkstraShortestPath.getPath("a3", "a1");
        final List<String> vertexList = shortestPath.getVertexList();
        final double weight = shortestPath.getWeight();
        System.out.println(vertexList);
        System.out.println(weight);
    }
}
