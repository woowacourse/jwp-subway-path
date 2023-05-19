package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.FixtureForSubwayMapTest.낙성대;
import static subway.fixture.FixtureForSubwayMapTest.남태령;
import static subway.fixture.FixtureForSubwayMapTest.방배;
import static subway.fixture.FixtureForSubwayMapTest.사당_2호선;
import static subway.fixture.FixtureForSubwayMapTest.사당_4호선;
import static subway.fixture.FixtureForSubwayMapTest.총신대입구;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;
import subway.business.domain.line.Station;

public class GraphPractice {

    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(낙성대);
        graph.addVertex(사당_2호선);
        graph.addVertex(방배);
        graph.addVertex(남태령);
        graph.addVertex(사당_4호선);
        graph.addVertex(총신대입구);
        graph.setEdgeWeight(graph.addEdge(낙성대, 사당_2호선), 5);
        graph.setEdgeWeight(graph.addEdge(사당_2호선, 방배), 2);
        graph.setEdgeWeight(graph.addEdge(남태령, 사당_4호선), 3);
        graph.setEdgeWeight(graph.addEdge(사당_4호선, 총신대입구), 6);
        graph.setEdgeWeight(graph.addEdge(사당_2호선, 사당_4호선), 0);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(낙성대, 총신대입구);
        List<Station> shortestPath = graphPath.getVertexList();
        List<DefaultWeightedEdge> edgeList = graphPath.getEdgeList();

        for (DefaultWeightedEdge edge : edgeList) {
            System.out.print("Source: " + graph.getEdgeSource(edge).getName() + " / ");
            System.out.print("Target: " + graph.getEdgeTarget(edge).getName() + " / ");
            System.out.println("Weight: " + graph.getEdgeWeight(edge));
        }

        for (Station station : shortestPath) {
            System.out.print(station.getName() + " - ");
        }
        assertThat(shortestPath.size()).isEqualTo(4);
    }
}
