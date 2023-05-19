package graph;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

class DijkstraTest {

    @Test
    void getDijkstraShortestPath() {
        final WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        final DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath
                = new DijkstraShortestPath<>(graph);
        final List<String> shortestPath
                = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        assertThat(shortestPath).hasSize(3);
    }

    @Test
    void getDijkstraShortestPath2() {
        final WeightedMultigraph<Integer, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.setEdgeWeight(graph.addEdge(1, 2), 2);
        graph.setEdgeWeight(graph.addEdge(2, 3), 2);
        graph.setEdgeWeight(graph.addEdge(1, 3), 100);

        final DijkstraShortestPath<Integer, DefaultWeightedEdge> dijkstraShortestPath
                = new DijkstraShortestPath<>(graph);
        final List<Integer> shortestPath
                = dijkstraShortestPath.getPath(3, 1).getVertexList();

        assertThat(shortestPath).hasSize(3);
    }

    @Test
    void 같은_정점을_여러번_더해도_문제_없다() {
        final WeightedMultigraph<Integer, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.setEdgeWeight(graph.addEdge(1, 2), 2);
        graph.setEdgeWeight(graph.addEdge(2, 3), 2);
        graph.setEdgeWeight(graph.addEdge(1, 3), 100);

        final DijkstraShortestPath<Integer, DefaultWeightedEdge> dijkstraShortestPath
                = new DijkstraShortestPath<>(graph);
        final List<Integer> shortestPath
                = dijkstraShortestPath.getPath(3, 1).getVertexList();

        assertThat(shortestPath).hasSize(3);
    }
}
