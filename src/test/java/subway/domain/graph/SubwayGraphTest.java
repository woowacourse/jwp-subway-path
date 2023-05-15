package subway.domain.graph;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.junit.jupiter.api.Test;
import subway.domain.edge.Edge;
import subway.domain.edge.Edges;
import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayGraphTest {

    private static final Station A = new Station(1L, "A");
    private static final Station B = new Station(2L, "B");
    private static final Station C = new Station(3L, "C");
    private static final Station D = new Station(4L, "D");
    private static final Station E = new Station(5L, "E");
    private static final Station F = new Station(6L, "F");
    private static final Station G = new Station(7L, "G");
    private static final Station H = new Station(8L, "H");

    @Test
    void getShortestPath() {
        // given
        final Map<Line, Edges> allEdges = Map.of(
                new Line(1L, "1호선"), new Edges(
                        List.of(
                                new Edge(1L, A, B, 3),
                                new Edge(2L, B, C, 4),
                                new Edge(3L, C, D, 5),
                                new Edge(4L, D, H, 5)

                        )
                ),
                new Line(2L, "2호선"), new Edges(
                        List.of(
                                new Edge(5L, B, C, 3),
                                new Edge(6L, C, D, 1),
                                new Edge(7L, D, H, 5)
                        )
                )
        );
        // when
        final DijkstraShortestPath<Station, Edge> shortestPath = SubwayGraph.getShortestPath(allEdges);
        final double pathWeight = shortestPath.getPathWeight(A, H);
        final List<Station> stations = shortestPath.getPath(A, H).getVertexList();

        // then
        assertThat(pathWeight).isEqualTo(12);
        assertThat(stations).containsExactly(A, B, C, D, H);
    }
}